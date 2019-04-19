package Solutions.Presentation.ControllerAdvice;

import org.reflections.Reflections;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

public class RestControllerAdviceManager {
    private static RestControllerAdviceManager instance;

    private List<RestControllerAdviceHandlerData> handlers;

    private RestControllerAdviceManager() throws ReflectiveOperationException {
        Reflections reflections = new Reflections();
        handlers = new ArrayList<>();
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(RestControllerAdvice.class)) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            handlers.addAll(Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RestControllerAdviceHandler.class))
                    .map(method -> new RestControllerAdviceHandlerData(method, instance)).collect(Collectors.toList()));
        }
        Collections.sort(handlers);
    }

    public static RestControllerAdviceManager getInstance() throws ReflectiveOperationException {
        if (instance == null)
            instance = new RestControllerAdviceManager();
        return instance;
    }

    public void handleException(Throwable e, HttpServletResponse resp) {

        for (RestControllerAdviceHandlerData handler : handlers)
            try {
                if(handler.handleException(e, resp))
                    return;
            } catch (Throwable e1) {
                e = e1;
            }

    }
}
