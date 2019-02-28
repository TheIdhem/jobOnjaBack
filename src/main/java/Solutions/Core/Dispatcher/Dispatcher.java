package Solutions.Core.Dispatcher;

import Solutions.Core.ApplicationProperties;
import Solutions.Core.Exceptions.BadRequest;
import Solutions.Core.Exceptions.UnAuthorized;
import Solutions.Data.Exceptions.EntityNotFound;
import Solutions.Presentation.Controller.HtmlController;
import Solutions.Presentation.Controller.RequestMapping;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;


public class Dispatcher implements HttpHandler {

    private static Dispatcher instance;
    private Set<EndPoint> endPoints;

    private Dispatcher() throws ReflectiveOperationException {
        endPoints = new HashSet<>();
        addHandlers(ApplicationProperties.getInstance().getProperty("solutions.controller.base_package"));
    }

    public static Dispatcher getInstance() throws ReflectiveOperationException {
        if (instance == null)
            instance = new Dispatcher();
        return instance;
    }

    @Override
    public void handle(HttpExchange _httpExchange) throws IOException {

        EnhancedHttpExchange httpExchange = new EnhancedHttpExchange(_httpExchange);

        RequestMethod requestMethod = RequestMethod.valueOf(httpExchange.getRequestMethod());
        String path = httpExchange.getRequestURI().getPath();

        Optional<EndPoint> handler = endPoints.stream().sorted(comparing(EndPoint::getPriority))
                .filter(endPoint -> endPoint.matches(requestMethod, path)).findFirst();
        String response;
        if (!handler.isPresent()) {
            response = "Page not found";
            httpExchange.sendResponseHeaders(404, response.getBytes().length);
        } else {

            try {
                response = handler.get().invoke(httpExchange);
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
            } catch (UnAuthorized e) {
                response = "Un-authorized: " + e.getMessage();
                httpExchange.sendResponseHeaders(403, response.getBytes().length);
            } catch (EntityNotFound e1) {
                response = "UNPROCESSABLE ENTITY";
                httpExchange.sendResponseHeaders(422, response.getBytes().length);
            } catch (BadRequest e2) {
                response = e2.getMessage();
                httpExchange.sendResponseHeaders(400, response.getBytes().length);
            } catch (Throwable e3) {
                response = "An error occurred.";
                httpExchange.sendResponseHeaders(500, response.getBytes().length);
                e3.printStackTrace();
            }
        }

        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }


    private void addHandlers(String basePackage) throws ReflectiveOperationException {

        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(HtmlController.class);

        for (Class<?> clazz : controllerClasses) {
            Object controllerInstance = clazz.getDeclaredConstructor().newInstance();

            Set<Method> classMethods = stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class)).collect(toSet());

            for (Method method : classMethods)
                endPoints.add(new EndPoint(method, controllerInstance));
        }

    }

}
