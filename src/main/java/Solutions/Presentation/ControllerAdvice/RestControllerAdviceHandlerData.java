package Solutions.Presentation.ControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RestControllerAdviceHandlerData implements Comparable<RestControllerAdviceHandlerData> {

    private Method method;
    private Object handlerInstance;
    private RestControllerAdviceHandler annotation;

    public RestControllerAdviceHandlerData(Method method, Object handlerInstance) {
        this.method = method;
        this.handlerInstance = handlerInstance;
        this.annotation = method.getAnnotation(RestControllerAdviceHandler.class);
    }

    @Override
    public int compareTo(RestControllerAdviceHandlerData that) {
        Class<?> thatClazz = that.method.getParameters()[0].getType();
        Class<?> thisClazz = this.method.getParameters()[0].getType();

        int result = thisClazz.equals(thatClazz) ? 0 :
                thisClazz.isAssignableFrom(thatClazz) ? 1 :-1;
        if (result != 0)
            return result;
        return this.annotation.order() - that.annotation.order();
    }

    public boolean handleException(Throwable e, HttpServletResponse response) throws Throwable {
        if (!method.getParameters()[0].getType().isAssignableFrom(e.getClass()))
            return false;
        try {
            Object result = method.invoke(handlerInstance, e);
            if (result == null)
                return false;
            response.setStatus(annotation.httpStatus());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write((new ObjectMapper()).writeValueAsString(result));

        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
        return true;
    }



}
