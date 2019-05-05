package Solutions.Data;

import org.springframework.core.GenericTypeResolver;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class EntityRepository<T extends Entity, ID_TYPE extends Serializable> {

    @SuppressWarnings("unchecked")
    public Class<ID_TYPE> getIdType() {

        return (Class<ID_TYPE>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(
                getClass(),
                EntityRepository.class
        ))[1];
    }

    public T save(T item){
        return null;
    }

    public Set<T> getAll(){
       return null;
    }

}
