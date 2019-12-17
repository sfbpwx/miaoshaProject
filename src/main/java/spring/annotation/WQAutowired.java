package spring.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WQAutowired {
    String value() default "";
}
