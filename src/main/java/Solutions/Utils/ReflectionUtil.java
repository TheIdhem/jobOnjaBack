package Solutions.Utils;

import Solutions.Data.Annotations.Id;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;

import static java.util.Arrays.stream;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFields;

public class ReflectionUtil {
    public static Field getIdField(Class<?> clazz){
        return stream(getAllFields(clazz))
                .filter(f -> f.isAnnotationPresent(Id.class)).findFirst()
                .orElseThrow(() -> new RuntimeException("Undefined id"));
    }




    public static Object getFieldValue(Field f, Object o) throws ReflectiveOperationException, IntrospectionException {

        boolean isAccessible = f.isAccessible();
        f.setAccessible(true);
        Object r =  f.get(o);
        f.setAccessible(isAccessible);
        return r;
    }

    public static void setField(Field f, Object o, Object value) throws ReflectiveOperationException, IntrospectionException {

        boolean isAccessible = f.isAccessible();
        f.setAccessible(true);
        f.set(o, value);
        f.setAccessible(isAccessible);
    }


}
