package com.inspireme.controller.converters;

import com.inspireme.model.Article;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class ObjectToIdConverterTest {

    private final ObjectToIdConverter objectToIdConverter = new ObjectToIdConverter();

      @Test
    public void happyPath_articleWithIdAnnotatedValue_returnsIdAnnotatedValue(){
        Article article = Article.builder().articleId(5L).build();

        assertThat(objectToIdConverter.convert(article)).isEqualTo("5");
    }

    @Test
    public void articleWithNullIdAnnotatedValue_throwsException(){
        Article article = Article.builder().build();

        assertThatThrownBy(() -> objectToIdConverter.convert(article))
                .hasMessage("HateOs Conversion Error: Cannot get value from @Id annotated field");
    }

    @Test
    public void objectWithoutIdAnnotatedValue_throwsException(){

        assertThatThrownBy(() -> objectToIdConverter.convert(new String()))
                .hasMessage("HateOs Conversion Error: No @Id annotated field");
    }
}
