package com.inspireme.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Field;

/**
 * Fix hateos mapping problem
 * See {@linktourl https://stackoverflow.com/questions/22240155/converter-from-pathvariable-domainobject-to-string-using-controllerlinkbuilde}
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext beanFactory;

    @Override
    public void addFormatters(final FormatterRegistry registry) {

        try {
            Class<?> clazz = Class.forName("org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor$BoundMethodParameter");
            Field field = clazz.getDeclaredField("CONVERSION_SERVICE");
            field.setAccessible(true);
            DefaultFormattingConversionService service = (DefaultFormattingConversionService) field.get(null);
            for (Converter<?, ?> converter : beanFactory.getBeansOfType(Converter.class).values()) {
                service.addConverter(converter);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
