package Solutions.Data.Proxy.Manipulation;


import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Map;

import static Solutions.Utils.ReflectionUtil.getFieldValue;
import static Solutions.Utils.ReflectionUtil.setField;

public class ModelProxyFieldAccessors {




    public static void setTable(Object o, String tableName) throws ReflectiveOperationException, IntrospectionException {
        setField(o.getClass().getDeclaredField("__table"), o, tableName);
    }

    public static String getTable(Object o) throws ReflectiveOperationException, IntrospectionException {
       return (String) getFieldValue(o.getClass().getDeclaredField("__table"), o);
    }

    static void initColumn(Object o) throws ReflectiveOperationException, IntrospectionException {
        setField(o.getClass().getDeclaredField("__columns"), o, new HashMap<String, String>());

    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getColumns(Object o) throws ReflectiveOperationException, IntrospectionException {
        try{
            return (Map<String, String>) getFieldValue( o.getClass().getDeclaredField("__columns"), o);
        }catch (NoSuchFieldException e){
            return null;
        }
    }



}
