package Solutions.Data;

import org.springframework.core.GenericTypeResolver;

import java.util.Objects;
import java.util.Set;

public interface ModelRepository<T> {

    T save(T o);

    Set<T> getAll();

    @SuppressWarnings("unchecked")
    default Class<T> getContentType() {
        return (Class<T>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(
                getClass(),
                EntityRepository.class
        ))[0];
    }
}
