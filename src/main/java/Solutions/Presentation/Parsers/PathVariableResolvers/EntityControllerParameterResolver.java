package Solutions.Presentation.Parsers.PathVariableResolvers;

import Solutions.Data.Entity;
import Solutions.Data.EntityManager;
import Solutions.Presentation.Controller.ControllerParameterResolver;

import java.io.Serializable;
import java.util.Optional;

public class EntityControllerParameterResolver implements ControllerParameterResolver<Entity> {

    private final EntityManager entityManager = EntityManager.getInstance();


    @Override
    public Optional<? extends Entity> resolve(String param, Class<? extends Entity> contextType) {
        Class<? extends Serializable> idType = entityManager.getIdType(contextType);
        Serializable id;
        if (idType.equals(String.class))
            id = param;
        else if (idType.equals(Integer.class))
            id = Integer.valueOf(param);
        else
            throw new RuntimeException("Unexpected id type:" + idType.getName());
        return entityManager.find(contextType, id);
    }

    @Override
    public String getRegex(Class<? extends Entity> contextType) {
        Class<? extends Serializable> idType = entityManager.getIdType(contextType);
        if (idType.isAssignableFrom(String.class))
            return "[^/]+";
        else if (idType.isAssignableFrom(Integer.class))
            return "\\d+";
        else
            throw new RuntimeException("Unexpected id type:" + idType.getName());
    }

    @Override
    public Class<Entity> getType() {
        return Entity.class;
    }
}
