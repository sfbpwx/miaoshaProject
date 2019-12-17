package spring.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WQService {
    String value() default "";
}
