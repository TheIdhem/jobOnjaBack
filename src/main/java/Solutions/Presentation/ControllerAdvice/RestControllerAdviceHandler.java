package Solutions.Presentation.ControllerAdvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestControllerAdviceHandler {
    int order() default Integer.MAX_VALUE;
    int httpStatus();
}
