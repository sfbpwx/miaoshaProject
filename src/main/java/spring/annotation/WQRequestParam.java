package spring.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WQRequestParam {
    String value() default "";
}
