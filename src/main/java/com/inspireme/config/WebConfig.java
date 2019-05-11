package com.inspireme.config;

import com.inspireme.controller.converters.ObjectToIdConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Field;


@Configuration
//@EnableWebMvc
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    private final ObjectToIdConverter objectToIdConverter = new ObjectToIdConverter();

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        getHateOsConversionService().addConverter(objectToIdConverter);
    }

    /**
     * Fix hateos mapping problem, manually adding my custom objectToIdConverter to hateOs CONVERSION_SERVICE
     * See {@linktourl https://github.com/spring-projects/spring-hateoas/issues/118}
     * See {@linktourl https://stackoverflow.com/questions/22240155/converter-from-pathvariable-domainobject-to-string-using-controllerlinkbuilde}
     */
    private DefaultFormattingConversionService getHateOsConversionService(){
        try {
            Class<?> clazz = Class.forName("org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor$BoundMethodParameter");
            Field field = clazz.getDeclaredField("CONVERSION_SERVICE");
            field.setAccessible(true);
            return (DefaultFormattingConversionService) field.get(null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/WEB-INF/view/");
//        resolver.setSuffix(".jsp");
//        resolver.setViewClass(JstlView.class);
//        registry.viewResolver(resolver);
//    }

//    @Bean
//    public ViewResolver internalResourceViewResolver() {
//        InternalResourceViewResolver bean = new InternalResourceViewResolver();
//        bean.setViewClass(JstlView.class);
//        bean.setPrefix("/WEB-INF/view/");
//        bean.setSuffix(".jsp");
//        return bean;
//    }

//    @Override //works for html only views
//    public void configureDefaultServletHandling(
//            DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
}
