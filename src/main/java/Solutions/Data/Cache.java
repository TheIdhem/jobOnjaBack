package Solutions.Data;



import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static Cache instance;

    private Map<Pair<Class, Serializable>, Object> store;
    public static Cache getInstance() {
        if (instance == null)
            instance = new Cache();
        return instance;
    }
    private Cache(){
        store = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> T assertObject(Class<T> clazz, Serializable id, T proxy){
        T cached = (T) store.get(new ImmutablePair<Class, Serializable>(clazz, id));
        if (cached == null) {
            store.put(new ImmutablePair<>(clazz, id), proxy);
            return proxy;
        }else
            return cached;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(Class<T> clazz, Serializable id){
        return (T) store.get(new ImmutablePair<Class, Serializable>(clazz, id));
    }

}
