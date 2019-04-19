package Solutions.Core.Dispatcher;

import Solutions.Core.Exceptions.MissingParameter;
import Solutions.Data.Exceptions.EntityNotFound;
import Solutions.Presentation.Controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;


class EndPoint {

    private final PathVariableResolverManager resolverManager;
    private String regex;
    private List<String> variableIds;
    private Method method;
    private Object controllerInstance;
    private ObjectMapper objectMapper;


    EndPoint(Method method, Object controllerInstance) throws ReflectiveOperationException {

        variableIds = new ArrayList<>();
        resolverManager = PathVariableResolverManager.getInstance();
        this.method = method;
        this.controllerInstance = controllerInstance;

        initialVariableIds();
        regex = generateRegex();
        objectMapper = new ObjectMapper();


    }

    private void initialVariableIds() {

        Pattern pattern = Pattern.compile("\\{.*?}");
        Matcher matcher = pattern.matcher(getPath());
        while (matcher.find())
            variableIds.add(removeStart(removeEnd(matcher.group(), "}"), "{"));

    }

    private Map<String, Class<?>> getVariableIdTypes() {

        return stream(method.getParameters()).filter(param -> param.isAnnotationPresent(PathVariable.class))
                .collect(toMap(param ->
                                param.getAnnotation(PathVariable.class).value(),
                        Parameter::getType
                ));

    }

    @SuppressWarnings("unchecked")
    private String generateRegex() {

        String regex = getPath();

        Map<String, Class<?>> variableIdTypes = getVariableIdTypes();
        for (String variableId : this.variableIds) {
            Class<?> contextClass = variableIdTypes.get(variableId);
            if (contextClass == null)
                throw new RuntimeException("Invalid variable id.");
            ControllerParameterResolver resolver = resolverManager.findClosestPathResolver(contextClass);
            if (resolver == null)
                throw new RuntimeException("Could not find appropriate resolver.");
            regex = regex.replace('{' + variableId + '}', "(" + resolver.getRegex(contextClass) + ")");
        }

        return regex;
    }

    Boolean matches(RequestMethod method, String requestUri) {
        return getRequestMethod().equals(method) && requestUri.matches(regex);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getValuations(String requestUri) {

        Map<String, Object> valuations = new HashMap<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(requestUri);
        if (!matcher.find())
            throw new RuntimeException("End point does not match.");
        Map<String, Class<?>> variableIdTypes = getVariableIdTypes();

        for (int i = 0; i < variableIds.size(); i++) {
            Class<?> contextClass = variableIdTypes.get(variableIds.get(i));
            ControllerParameterResolver resolver = resolverManager.findClosestPathResolver(contextClass);
            Optional o = resolver.resolve(matcher.group(i + 1), contextClass);
            valuations.put(variableIds.get(i), o.orElse(null));
        }
        return valuations;
    }

    @SuppressWarnings("unchecked")
    private Object convert(String var, Class<?> context) {
        if (var == null)
            return null;
        ControllerParameterResolver resolver = resolverManager.findClosestPathResolver(context);
        if (resolver == null)
            throw new RuntimeException("Could not find appropriate resolver.");
        return resolver.resolve(var, context).orElse(null);
    }

    void invoke(HttpServletRequest req, HttpServletResponse resp) throws Throwable {

        Map<String, Object> valuations = getValuations(req.getRequestURI());

        Object args[] = new Object[method.getParameterCount()];

        for (int i = 0; i < method.getParameterCount(); i++) {

            Parameter parameter = method.getParameters()[i];

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);

                if (valuations.get(pathVariable.value()) == null)
                    throw new EntityNotFound(parameter.getType(), pathVariable.value());

                args[i] = valuations.get(pathVariable.value());

            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);

                if (req.getParameter(requestParam.value()) == null) {
                    if (requestParam.required())
                        throw new MissingParameter(requestParam.value());
                    else
                        args[i] = null;
                } else {
                    args[i] = convert(req.getParameter(requestParam.value()), parameter.getType());
                    if (args[i] == null)
                        throw new EntityNotFound(parameter.getType(), requestParam.value());
                }
            }else if (parameter.isAnnotationPresent(RequestBody.class)) {

                args[i] = objectMapper.readValue(req.getReader(), parameter.getType());
            } else
                throw new RuntimeException("Unexpected parameter for method.");
        }
        try {
            Object result = method.invoke(controllerInstance, args);
            String response = objectMapper.writeValueAsString(result);
            resp.setStatus(200);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(response);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    int getPriority() {
        return Integer.MAX_VALUE - regex.length();
    }

    private String getPath() {
        return method.getDeclaringClass().getAnnotation(RestController.class).basePath() +
                method.getAnnotation(RequestMapping.class).path();
    }

    private RequestMethod getRequestMethod() {
        return method.getAnnotation(RequestMapping.class).method();
    }

}
