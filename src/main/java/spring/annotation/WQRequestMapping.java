package spring.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WQRequestMapping {
    String[] value() default "";

    RequestMethod[] method() default {};
}
