package com.inspireme.config;

import com.inspireme.controller.converters.ObjectToIdConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Field;


@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    private final ObjectToIdConverter objectToIdConverter = new ObjectToIdConverter();

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        getHateOsConversionService().addConverter(objectToIdConverter);
    }

    private DefaultFormattingConversionService getHateOsConversionService(){
        try {
            Class<?> klass = Class.forName
                    ("org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor$BoundMethodParameter");
            Field field = klass.getDeclaredField("CONVERSION_SERVICE");
            field.setAccessible(true);
            return (DefaultFormattingConversionService) field.get(null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
