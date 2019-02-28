package Solutions.Data;

import org.springframework.core.GenericTypeResolver;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public interface EntityRepository<T extends Entity, ID_TYPE extends Serializable> extends ModelRepository<T> {

    default Optional<T> getById(Serializable id) {
        return getAll().stream().filter(item -> item.getId().equals(id)).findFirst();
    }

    @SuppressWarnings("unchecked")
    default Class<ID_TYPE> getIdType() {

        return (Class<ID_TYPE>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(
                getClass(),
                EntityRepository.class
        ))[1];
    }

}
