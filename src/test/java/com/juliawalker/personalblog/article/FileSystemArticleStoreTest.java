package com.juliawalker.personalblog.article;

import static com.juliawalker.personalblog.article.ArticleFixtures.NOW;
import static com.juliawalker.personalblog.article.ArticleFixtures.article;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSystemArticleStoreTest {

    @TempDir
    Path directory;

    @Test
    void constructor_missingDirectory_createsIt() {
        Path nested = directory.resolve("a/b/articles");

        FileSystemArticleStore store = new FileSystemArticleStore(nested);

        assertThat(nested).isDirectory();
        assertThat(store.findAll()).isEmpty();
    }

    @Test
    void save_newArticle_writesFileNamedById() {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);
        Article article = article(ArticleId.generate(), "hello", NOW);

        Article saved = store.save(article);

        assertThat(directory.resolve(article.getId().asString() + ".md")).isRegularFile();
        assertThat(store.findById(article.getId())).contains(saved);
        assertThat(store.findBySlug(new Slug("hello"))).contains(saved);
    }

    @Test
    void save_thenNewStoreOnSameDirectory_reloadsArticles() {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);
        Article saved = store.save(article(ArticleId.generate(), "persisted", NOW));

        FileSystemArticleStore reloaded = new FileSystemArticleStore(directory);

        assertThat(reloaded.findAll()).containsExactly(saved);
    }

    @Test
    void save_existingArticle_updatesFileAndSlugIndex() {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);
        Article saved = store.save(article(ArticleId.generate(), "old-slug", NOW));
        Article revised = saved.revise(new ArticleTitle("New"), new Slug("new-slug"), null,
                new ArticleContent("New body"), null, List.of(), null, NOW.plusHours(1));

        Article updated = store.save(revised);

        assertThat(store.findBySlug(new Slug("old-slug"))).isEmpty();
        assertThat(store.findBySlug(new Slug("new-slug"))).contains(updated);
        assertThat(new FileSystemArticleStore(directory).findById(saved.getId())).contains(updated);
    }

    @Test
    void save_slugUsedByAnotherArticle_throwsArticleAlreadyExistsException() {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);
        store.save(article(ArticleId.generate(), "taken", NOW));

        assertThatThrownBy(() -> store.save(article(ArticleId.generate(), "taken", NOW)))
                .isInstanceOf(ArticleAlreadyExistsException.class);
        assertThat(store.findAll()).hasSize(1);
    }

    @Test
    void save_success_leavesNoTemporaryFiles() throws IOException {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);

        store.save(article(ArticleId.generate(), "clean", NOW));

        try (Stream<Path> files = Files.list(directory)) {
            assertThat(files).allMatch(file -> file.getFileName().toString().endsWith(".md")
                    && !file.getFileName().toString().startsWith("."));
        }
    }

    @Test
    void delete_existingArticle_removesFileAndIndex() {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);
        Article saved = store.save(article(ArticleId.generate(), "doomed", NOW));

        boolean deleted = store.delete(saved.getId());

        assertThat(deleted).isTrue();
        assertThat(directory.resolve(saved.getId().asString() + ".md")).doesNotExist();
        assertThat(store.findById(saved.getId())).isEmpty();
        assertThat(store.findBySlug(new Slug("doomed"))).isEmpty();
    }

    @Test
    void delete_unknownArticle_returnsFalse() {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);

        assertThat(store.delete(ArticleId.generate())).isFalse();
    }

    @Test
    void constructor_malformedFile_skipsItAndLoadsOthers() throws IOException {
        Article valid = new FileSystemArticleStore(directory).save(article(ArticleId.generate(), "valid", NOW));
        Files.writeString(directory.resolve(ArticleId.generate().asString() + ".md"), "not an article",
                StandardCharsets.UTF_8);

        FileSystemArticleStore store = new FileSystemArticleStore(directory);

        assertThat(store.findAll()).containsExactly(valid);
    }

    @Test
    void constructor_readableFileName_loadsAndPreservesPathOnSave() throws IOException {
        Article saved = new FileSystemArticleStore(directory).save(article(ArticleId.generate(), "moved", NOW));
        Path readableFile = directory.resolve("english-readable-article.md");
        Files.move(directory.resolve(saved.getId().asString() + ".md"), readableFile);

        FileSystemArticleStore store = new FileSystemArticleStore(directory);
        store.save(saved.revise(new ArticleTitle("Updated"), saved.getSlug(), null,
                new ArticleContent("Updated body"), null, List.of(), NOW, NOW.plusHours(1)));

        assertThat(store.findAll()).hasSize(1);
        assertThat(readableFile).isRegularFile();
        assertThat(directory.resolve(saved.getId().asString() + ".md")).doesNotExist();
    }

    @Test
    void constructor_twoFilesWithSameSlug_keepsOnlyOne() throws IOException {
        ArticleFileFormat fileFormat = new ArticleFileFormat();
        Article first = article(ArticleId.generate(), "same", NOW);
        Article second = article(ArticleId.generate(), "same", NOW);
        Files.writeString(directory.resolve(first.getId().asString() + ".md"), fileFormat.write(first));
        Files.writeString(directory.resolve(second.getId().asString() + ".md"), fileFormat.write(second));

        FileSystemArticleStore store = new FileSystemArticleStore(directory);

        assertThat(store.findAll()).hasSize(1);
    }

    @Test
    void constructor_ignoresHiddenAndNonMarkdownFiles() throws IOException {
        Files.writeString(directory.resolve(".leftover.md.tmp"), "partial");
        Files.writeString(directory.resolve("notes.txt"), "text");

        FileSystemArticleStore store = new FileSystemArticleStore(directory);

        assertThat(store.findAll()).isEmpty();
    }

    @Test
    void save_concurrentCreatesWithDistinctSlugs_storesAll() throws Exception {
        FileSystemArticleStore store = new FileSystemArticleStore(directory);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Future<Article>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < 40; i++) {
                String slug = "concurrent-" + i;
                futures.add(executor.submit(() -> store.save(article(ArticleId.generate(), slug, NOW))));
            }
            for (Future<Article> future : futures) {
                future.get();
            }
        } finally {
            executor.shutdownNow();
        }

        assertThat(store.findAll()).hasSize(40);
        assertThat(new FileSystemArticleStore(directory).findAll()).hasSize(40);
    }

}
