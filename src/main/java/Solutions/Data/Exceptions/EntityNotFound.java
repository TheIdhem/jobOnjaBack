package Solutions.Data.Exceptions;

import java.io.Serializable;

public class EntityNotFound extends RuntimeException {

    private Class<?> entityType;
    private Serializable id;

    public EntityNotFound() {
    }

    public EntityNotFound(Class<?> entityType, Serializable id) {
        this.entityType = entityType;
        this.id = id;
    }

    public Class<?> getEntityType() {
        return entityType;
    }

    public Serializable getId() {
        return id;
    }
}
