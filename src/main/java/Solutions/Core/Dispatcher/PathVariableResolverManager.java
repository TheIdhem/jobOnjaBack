package Solutions.Core.Dispatcher;

import Solutions.Core.ApplicationProperties;
import Solutions.Presentation.Controller.ControllerParameterResolver;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

class PathVariableResolverManager {

    private static PathVariableResolverManager instance;
    private Set<ControllerParameterResolver> controllerParameterResolvers;

    private PathVariableResolverManager() throws ReflectiveOperationException {

        controllerParameterResolvers = new HashSet<>();
        addPathVariableResolvers("Solutions.Presentation.Parsers.PathVariableResolvers");
        addPathVariableResolvers(ApplicationProperties.getInstance()
                .getProperty("solutions.controller.controller_parameter_resolver.base_package"));
    }

    public static PathVariableResolverManager getInstance() throws ReflectiveOperationException {
        if (instance == null)
            instance = new PathVariableResolverManager();
        return instance;
    }

    ControllerParameterResolver findClosestPathResolver(Class<?> clazz) {

        if (clazz.equals(Object.class))
            return null;
        for (ControllerParameterResolver resolver : controllerParameterResolvers)
            if (clazz.equals(resolver.getType()))
                return resolver;

        for (Class<?> _interface : clazz.getInterfaces())
            for (ControllerParameterResolver resolver : controllerParameterResolvers) {
                Class c = resolver.getType();
                if (_interface.equals(c))
                    return resolver;
            }
        return findClosestPathResolver(clazz.getSuperclass());
    }

    private void addPathVariableResolvers(String basePath) throws ReflectiveOperationException {

        Set<Class<? extends ControllerParameterResolver>> classes =
                new Reflections(basePath).getSubTypesOf(ControllerParameterResolver.class);

        for (Class<? extends ControllerParameterResolver> clazz : classes)
            controllerParameterResolvers.add(clazz.getDeclaredConstructor().newInstance());
    }
}
