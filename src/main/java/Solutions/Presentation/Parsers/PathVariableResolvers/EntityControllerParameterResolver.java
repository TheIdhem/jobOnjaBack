package Solutions.Presentation.Parsers.PathVariableResolvers;

import Solutions.Data.Entity;
import Solutions.Data.EntityManager;
import Solutions.Presentation.Controller.ControllerParameterResolver;
import Solutions.Utils.ObjectConverter;

import java.io.Serializable;
import java.util.Optional;

import static Solutions.Utils.ReflectionUtil.getIdField;

public class EntityControllerParameterResolver implements ControllerParameterResolver<Entity> {

    private final EntityManager entityManager = EntityManager.getInstance();


    @Override
    public Optional<? extends Entity> resolve(String param, Class<? extends Entity> contextType) throws Exception {
        Class<?> idType = getIdField(contextType).getType();
        Serializable id = (Serializable) ObjectConverter.stringToSerializable(param, idType);
        return entityManager.find(contextType, id);
    }

    @Override
    public String getRegex(Class<? extends Entity> contextType) {
        Class<?> idType = getIdField(contextType).getType();
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
