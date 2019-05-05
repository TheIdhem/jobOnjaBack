package Solutions.Data;


import Solutions.Data.Annotations.ManyToOne;
import Solutions.Data.Proxy.Manipulation.ModelProxyFactory;
import Solutions.Data.Proxy.Manipulation.ModelProxyFieldAccessors;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;

import static Solutions.Utils.ReflectionUtil.setField;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFields;


public class BeanMapper {


    public <T> T getForObject(ResultSet rs, String tableName, Class<T> clazz) throws Exception {
        T result = ModelProxyFactory.create(clazz, getClass().getClassLoader());

        ModelProxyFieldAccessors.setTable(result, tableName);
        for (Field f : getAllFields(clazz)){

            if (f.getName().startsWith("__"))
                continue;

            if (Serializable.class.isAssignableFrom(f.getType())){
                Object value = rs.getObject(f.getName());
                setField(f, result, value);
            }

            if (f.isAnnotationPresent(ManyToOne.class))
                ModelProxyFieldAccessors.getColumns(result).put(f.getName(), rs.getString(f.getName()));
        }

        return result;
    }

}
