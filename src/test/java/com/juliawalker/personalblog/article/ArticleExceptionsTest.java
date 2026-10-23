package com.juliawalker.personalblog.article;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ArticleExceptionsTest {

    @Test
    void articleNotFoundException_byId_includesIdAsMessageArgument() {
        ArticleId articleId = ArticleId.generate();

        ArticleNotFoundException exception = new ArticleNotFoundException(articleId);

        assertThat(exception.getMessageKey()).isEqualTo("article.error.notFound.byId");
        assertThat(exception.getArgs()).containsExactly(articleId.asString());
    }

    @Test
    void articleNotFoundException_bySlug_includesSlugAsMessageArgument() {
        ArticleNotFoundException exception = new ArticleNotFoundException(new Slug("missing"));

        assertThat(exception.getMessageKey()).isEqualTo("article.error.notFound.bySlug");
        assertThat(exception.getArgs()).containsExactly("missing");
    }

    @Test
    void articleAlreadyExistsException_slugAndTitle_useDistinctMessageKeys() {
        ArticleAlreadyExistsException bySlug = new ArticleAlreadyExistsException(new Slug("taken"));
        ArticleAlreadyExistsException byTitle = new ArticleAlreadyExistsException(new ArticleTitle("Taken"));

        assertThat(bySlug.getMessageKey()).isEqualTo("article.error.slugAlreadyExists");
        assertThat(bySlug.getArgs()).containsExactly("taken");
        assertThat(byTitle.getMessageKey()).isEqualTo("article.error.titleAlreadyExists");
        assertThat(byTitle.getArgs()).containsExactly("Taken");
    }

}
