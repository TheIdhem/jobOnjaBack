package Solutions.Data;

import Solutions.Core.ApplicationProperties;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EntityManager {

    private static EntityManager instance;

    private Map<Class<? extends Entity>, EntityRepository<?, ? extends Serializable>> classToRepoMap;

    private EntityManager() {
        classToRepoMap = new HashMap<>();
        String basePackage = ApplicationProperties.getInstance()
                .getProperty("solutions.data.repositories.base_package");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends EntityRepository>> subTypes = reflections.getSubTypesOf(EntityRepository.class);
        for (Class<? extends EntityRepository> clazz : subTypes) {
            try {
                EntityRepository<? extends Entity, ?> entityRepository =
                        (EntityRepository<? extends Entity, ?>) clazz.getMethod("getInstance").invoke(null);
                classToRepoMap.put(entityRepository.getContentType(), entityRepository);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static EntityManager getInstance() {
        if (instance == null)
            instance = new EntityManager();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> Optional<T> find(Class<T> clazz, Serializable id) {

        EntityRepository<?, ?> repository = classToRepoMap.get(clazz);
        return (Optional<T>) repository.getById(id);
    }

    public Class<? extends Serializable> getIdType(Class<? extends Entity> clazz) {
        return classToRepoMap.get(clazz).getIdType();
    }
}
