package com.inspireme.controller.converters;

import org.springframework.core.convert.converter.Converter;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

/**
 * Converter used by hateOs for link creation
 */
public class ObjectToIdConverter implements Converter<Object, String> {

    @Override
    public String convert(Object obj) {

        return Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .map(field -> extractFieldValue(field, obj))
                .orElseThrow(() -> new RuntimeException("HateOs Conversion Error: No @Id annotated field"));
    }

    private String extractFieldValue(Field field, Object obj) {
        try {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            Object fieldValue = field.get(obj);
            return Optional.ofNullable(fieldValue)
                    .map(String::valueOf)
                    .orElseThrow(() -> new RuntimeException("HateOs Conversion Error: Cannot get value from @Id annotated field"));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("HateOs Conversion Error", e);
        }
    }
}