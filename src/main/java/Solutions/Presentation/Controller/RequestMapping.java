package Solutions.Presentation.Controller;

import Solutions.Core.Dispatcher.RequestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {METHOD})
@Retention(value = RUNTIME)
public @interface RequestMapping {
    String path() default "";

    String template();

    RequestMethod method();
}
