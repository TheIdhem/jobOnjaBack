package Solutions.Presentation.Parsers;

import Solutions.Data.Entity;
import Solutions.Data.EntityManager;
import Solutions.Data.Exceptions.EntityNotFound;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

import java.io.Serializable;

public class EntityObjectIdResolver extends SimpleObjectIdResolver {

    private final EntityManager entityManager;

    public EntityObjectIdResolver() {
        this.entityManager = EntityManager.getInstance();
    }

    @Override
    public void bindItem(IdKey id, Object pojo) {
        super.bindItem(id, pojo);
    }

    @Override
    public Object resolveId(IdKey id) {
        Object resolved = super.resolveId(id);
        if (resolved == null) {
            resolved = tryToLoadFromSource(id);
            bindItem(id, resolved);
        }

        return resolved;
    }

    @SuppressWarnings("unchecked")
    private Object tryToLoadFromSource(IdKey idKey) {

        Serializable id = (Serializable) idKey.key;
        Class<? extends Entity> entityType = (Class<? extends Entity>) idKey.scope;

        try {
            return entityManager.find(entityType, id).orElseThrow(() -> new EntityNotFound(entityType, id));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new EntityObjectIdResolver();
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == EntityObjectIdResolver.class;
    }

}
