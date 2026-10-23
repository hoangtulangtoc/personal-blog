package com.juliawalker.personalblog.article;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores articles as Markdown files and keeps them in an in-memory index.
 * New articles use {@code {id}.md}; files loaded from disk retain their existing paths.
 * Only one application instance may use a directory.
 */
public class FileSystemArticleStore implements ArticleStore {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemArticleStore.class);

    private final Path directory;
    private final ArticleFileFormat fileFormat;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<ArticleId, Article> articlesById = new HashMap<>();
    private final Map<Slug, ArticleId> idsBySlug = new HashMap<>();
    private final Map<ArticleId, Path> filesById = new HashMap<>();

    public FileSystemArticleStore(Path directory) {
        this(directory, new ArticleFileFormat());
    }

    FileSystemArticleStore(Path directory, ArticleFileFormat fileFormat) {
        this.directory = directory.toAbsolutePath().normalize();
        this.fileFormat = fileFormat;
        try {
            Files.createDirectories(this.directory);
        } catch (IOException e) {
            throw new ArticleStorageException("Cannot create article directory " + this.directory, e);
        }
        loadIndex();
    }

    @Override
    public ArticleId nextId() {
        return ArticleId.generate();
    }

    @Override
    public List<Article> findAll() {
        lock.readLock().lock();
        try {
            return List.copyOf(articlesById.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<Article> findById(ArticleId articleId) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(articlesById.get(articleId));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<Article> findBySlug(Slug slug) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(idsBySlug.get(slug)).map(articlesById::get);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Article save(Article article) {
        lock.writeLock().lock();
        try {
            Article stored = articlesById.get(article.getId());
            ArticleId slugOwner = idsBySlug.get(article.getSlug());
            if (slugOwner != null && !slugOwner.equals(article.getId())) {
                throw new ArticleAlreadyExistsException(article.getSlug());
            }
            Path file = filesById.getOrDefault(article.getId(), fileOf(article.getId()));
            writeAtomically(file, fileFormat.write(article));
            if (stored != null) {
                idsBySlug.remove(stored.getSlug());
            }
            articlesById.put(article.getId(), article);
            idsBySlug.put(article.getSlug(), article.getId());
            filesById.put(article.getId(), file);
            logger.info("Article saved: id={}", article.getId().asString());
            return article;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean delete(ArticleId articleId) {
        lock.writeLock().lock();
        try {
            Article stored = articlesById.get(articleId);
            if (stored == null) {
                return false;
            }
            try {
                Files.deleteIfExists(filesById.getOrDefault(articleId, fileOf(articleId)));
            } catch (IOException e) {
                throw new ArticleStorageException("Cannot delete article file for " + articleId.asString(), e);
            }
            articlesById.remove(articleId);
            idsBySlug.remove(stored.getSlug());
            filesById.remove(articleId);
            logger.info("Article deleted: id={}", articleId.asString());
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Path fileOf(ArticleId articleId) {
        return directory.resolve(articleId.asString() + ArticleFileFormat.FILE_EXTENSION);
    }

    // Write to a temporary file before moving it into place to avoid partial article files.
    private void writeAtomically(Path target, String text) {
        Path temp = null;
        try {
            temp = Files.createTempFile(directory, "." + target.getFileName(), ".tmp");
            Files.writeString(temp, text, StandardCharsets.UTF_8);
            try {
                Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            deleteQuietly(temp);
            throw new ArticleStorageException("Cannot write article file " + target.getFileName(), e);
        }
    }

    private void loadIndex() {
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*" + ArticleFileFormat.FILE_EXTENSION)) {
            stream.forEach(files::add);
        } catch (IOException e) {
            throw new ArticleStorageException("Cannot list article directory " + directory, e);
        }
        files.sort(null);
        for (Path file : files) {
            if (file.getFileName().toString().startsWith(".") || !Files.isRegularFile(file)) {
                continue;
            }
            loadFile(file);
        }
        logger.info("Loaded {} article(s) from {}", articlesById.size(), directory);
    }

    // Skip malformed files with a warning so they do not prevent the application from starting.
    private void loadFile(Path file) {
        Article article;
        try {
            article = fileFormat.read(Files.readString(file, StandardCharsets.UTF_8));
        } catch (IOException | RuntimeException e) {
            logger.warn("Skipping unreadable article file {}: {}", file.getFileName(), e.getMessage());
            return;
        }
        if (articlesById.containsKey(article.getId())) {
            logger.warn("Skipping article file {}: id '{}' already used by another file", file.getFileName(),
                    article.getId().asString());
            return;
        }
        if (idsBySlug.containsKey(article.getSlug())) {
            logger.warn("Skipping article file {}: slug '{}' already used by {}", file.getFileName(),
                    article.getSlug().asString(), idsBySlug.get(article.getSlug()).asString());
            return;
        }
        articlesById.put(article.getId(), article);
        idsBySlug.put(article.getSlug(), article.getId());
        filesById.put(article.getId(), file);
    }

    private static void deleteQuietly(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.warn("Cannot delete temporary file {}", path.getFileName());
        }
    }

}
