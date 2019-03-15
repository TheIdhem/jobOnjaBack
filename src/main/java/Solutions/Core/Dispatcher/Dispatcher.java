package Solutions.Core.Dispatcher;

import Solutions.Core.ApplicationProperties;
import Solutions.Core.Exceptions.IllegalFormat;
import Solutions.Core.Exceptions.MissingParameter;
import Solutions.Core.Exceptions.UnAuthorized;
import Solutions.Data.Exceptions.EntityNotFound;
import Solutions.Presentation.Controller.RestController;
import Solutions.Presentation.Controller.RequestMapping;
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

    public Dispatcher() throws ReflectiveOperationException {
        endPoints = new HashSet<>();
        addHandlers(ApplicationProperties.getInstance().getProperty("solutions.controller.base_package"));
    }


    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
        String path = req.getRequestURI();
        Optional<EndPoint> handler = endPoints.stream().sorted(comparing(EndPoint::getPriority))
                .filter(endPoint -> endPoint.matches(requestMethod, path)).findFirst();
        String response;
        if (!handler.isPresent()) {
            response = "Page not found";
            resp.setStatus(404);
        } else {

            try {
                response = handler.get().invoke(req, resp);
                resp.setStatus(200);
            } catch (UnAuthorized e) {
                response = "Un-authorized: " + e.getMessage();
                resp.setStatus(403);
            } catch (EntityNotFound e1) {
                response = "Unprocessable entity";
                resp.setStatus(422);
            } catch (MissingParameter | IllegalFormat e2) {
                response = e2.getMessage();
                resp.setStatus(400);
            }catch (Throwable e3) {
                response = "An error occurred.";
                resp.setStatus(500);
                e3.printStackTrace();
            }
        }

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(response);
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
