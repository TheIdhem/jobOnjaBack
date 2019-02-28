package Solutions.Core.Dispatcher;

import Solutions.Core.Exceptions.BadRequest;
import Solutions.Data.Exceptions.EntityNotFound;
import Solutions.Presentation.Controller.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
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


    EndPoint(Method method, Object controllerInstance) throws ReflectiveOperationException {

        variableIds = new ArrayList<>();
        resolverManager = PathVariableResolverManager.getInstance();
        this.method = method;
        this.controllerInstance = controllerInstance;

        initialVariableIds();
        regex = generateRegex();


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

    String invoke(EnhancedHttpExchange httpExchange) throws Throwable {

        Document document = null;

        Map<String, Object> valuations = getValuations(httpExchange.getRequestURI().getPath());

        Object args[] = new Object[method.getParameterCount()];

        for (int i = 0; i < method.getParameterCount(); i++) {

            Parameter parameter = method.getParameters()[i];

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);

                if (valuations.get(pathVariable.value()) == null)
                    throw new EntityNotFound();

                args[i] = valuations.get(pathVariable.value());

            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);

                if (httpExchange.getQueryParam(requestParam.value()) == null) {
                    if (requestParam.required())
                        throw new BadRequest(requestParam.value());
                    else
                        args[i] = null;
                } else {
                    args[i] = convert(httpExchange.getQueryParam(requestParam.value()), parameter.getType());
                    if (args[i] == null)
                        throw new EntityNotFound();
                }
            } else if (parameter.getType().equals(Document.class))
                args[i] = document = Jsoup.parse(getTemplate(), "UTF-8", "");
            else
                throw new RuntimeException("Unexpected parameter for method.");
        }
        try {
            method.invoke(controllerInstance, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }


        if (document != null)
            return document.html();
        return null;
    }

    int getPriority() {
        return Integer.MAX_VALUE - regex.length();
    }

    private String getPath() {
        return method.getDeclaringClass().getAnnotation(HtmlController.class).basePath() +
                method.getAnnotation(RequestMapping.class).path();
    }

    private RequestMethod getRequestMethod() {
        return method.getAnnotation(RequestMapping.class).method();
    }

    private InputStream getTemplate() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream("templates/" + method.getAnnotation(RequestMapping.class).template());
    }

}