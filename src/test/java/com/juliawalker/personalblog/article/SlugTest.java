package com.juliawalker.personalblog.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SlugTest {

    @Test
    void constructor_validSlug_keepsValue() {
        Slug slug = new Slug("hello-world_2");

        assertThat(slug.asString()).isEqualTo("hello-world_2");
    }

    @Test
    void constructor_uppercaseOrSpaces_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new Slug("Hello World")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_pathTraversal_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new Slug("../etc/passwd")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void fromText_vietnameseTitle_removesDiacritics() {
        Slug slug = Slug.fromText("Xin chào Đà Nẵng: Spring Boot 3!").orElseThrow();

        assertThat(slug.asString()).isEqualTo("xin-chao-da-nang-spring-boot-3");
    }

    @Test
    void fromText_onlySymbols_returnsEmpty() {
        assertThat(Slug.fromText("!!! ???")).isEmpty();
        assertThat(Slug.fromText(null)).isEmpty();
    }

    @Test
    void fromText_veryLongText_truncatesToMaxLength() {
        Slug slug = Slug.fromText("a-".repeat(300)).orElseThrow();

        assertThat(slug.asString().length()).isLessThanOrEqualTo(Slug.MAX_LENGTH);
        assertThat(slug.asString()).doesNotEndWith("-");
    }

}
