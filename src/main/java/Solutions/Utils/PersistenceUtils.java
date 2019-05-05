package Solutions.Utils;

import Solutions.Data.Proxy.ModelProxy;
import Solutions.Data.Proxy.Trackers.PersistenceSet;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static Solutions.Utils.ReflectionUtil.getFieldValue;
import static Solutions.Utils.ReflectionUtil.setField;
import static org.apache.commons.beanutils.PropertyUtils.copyProperties;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFields;

public class PersistenceUtils {
    
    public static <T> T unproxy(T object) throws Exception {
        Class<?> tClass =  object.getClass();

        for (Field f : getAllFields(tClass)){
            Object field = getFieldValue(f, object);
            if (field instanceof ModelProxy)
                setField(f, object, unproxy(field));
            else if (object instanceof PersistenceSet) {
                Set s = new HashSet();
                for (Object o : (Collection) field)
                    s.add(unproxy(o));
                setField(f, object, s);
            }
        }


        if (object instanceof ModelProxy) {
            T unproified = (T) object.getClass().getSuperclass().newInstance();
            copyProperties(unproified, object);
            return unproified;
        }
        else
            return object;
    }

}
