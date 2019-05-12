package Solutions.Core.Dispatcher;

import Solutions.Core.ApplicationProperties;
import Solutions.Core.Exceptions.NoSuchEndPoint;
import Solutions.Presentation.Controller.RestController;
import Solutions.Presentation.Controller.RequestMapping;
import Solutions.Presentation.ControllerAdvice.RestControllerAdviceManager;
import org.reflections.Reflections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;

@WebServlet("/*")
public class Dispatcher extends HttpServlet {

    private Set<EndPoint> endPoints;
    private final RestControllerAdviceManager restControllerAdviceManager;

    public Dispatcher() throws ReflectiveOperationException {
        endPoints = new HashSet<>();
        restControllerAdviceManager = RestControllerAdviceManager.getInstance();
        addHandlers(ApplicationProperties.getInstance().getProperty("solutions.controller.base_package"));

    }


    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
        String path = req.getRequestURI();
        Optional<EndPoint> handler = endPoints.stream().sorted(comparing(EndPoint::getPriority))
                .filter(endPoint -> endPoint.matches(requestMethod, path)).findFirst();
        try{
            if (!handler.isPresent()) {
                throw new NoSuchEndPoint(requestMethod, path);
            } else {
                handler.get().invoke(req, resp);
            }
        } catch (Throwable e) {
            restControllerAdviceManager.handleException(e, resp);
        }

    }


    private void addHandlers(String basePackage) throws ReflectiveOperationException {

        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(RestController.class);

        for (Class<?> clazz : controllerClasses) {
            Object controllerInstance = clazz.getDeclaredConstructor().newInstance();

            Set<Method> classMethods = stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class)).collect(toSet());

            for (Method method : classMethods)
                endPoints.add(new EndPoint(method, controllerInstance));
        }

    }

}
