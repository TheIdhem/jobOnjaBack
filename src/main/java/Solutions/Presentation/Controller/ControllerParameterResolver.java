package Solutions.Presentation.Controller;

import org.springframework.core.GenericTypeResolver;

import java.util.Optional;

public interface ControllerParameterResolver<T> {

    Optional<? extends T> resolve(String param, Class<? extends T> contextType);

    String getRegex(Class<? extends T> contextType);

    @SuppressWarnings("unchecked")
    default Class<T> getType() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(
                getClass(),
                ControllerParameterResolver.class
        );
    }
}
