package com.inspireme.controller.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Converter used by hateOs for link creation
 */
@Component
public class ObjectToIdConverter implements Converter<Object, String> {

    private final String EMPTY_STRING = "";

    @Override
    public String convert(Object obj) {

        return Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .map(field -> extractFieldValue(field, obj))
                .orElse(EMPTY_STRING);
    }

    private String extractFieldValue(Field field, Object obj){
        try {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            return String.valueOf(field.get(obj));
        } catch (IllegalAccessException e) {
//            throw new Exception("HateOs Conversion Error");
            return EMPTY_STRING;
        }
    }
}