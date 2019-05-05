package Solutions.Data.Proxy.Manipulation;

import Solutions.Data.Proxy.ModelProxy;
import Solutions.Data.Proxy.Trackers.GetterInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;

import java.beans.IntrospectionException;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.isGetter;

public class ModelProxyFactory {
    public static <T> T create(Class<T> clazz, ClassLoader loader) throws ReflectiveOperationException, IntrospectionException {
        T o = new ByteBuddy()
                .subclass(clazz)
                .defineField("__columns", Map.class, Visibility.PRIVATE)
                .defineField("__table", String.class, Visibility.PRIVATE)
                .method(isGetter())
                .intercept(MethodDelegation.to(GetterInterceptor.class))
                .implement(ModelProxy.class)
                .make()
                .load(loader)
                .getLoaded().newInstance();
        ModelProxyFieldAccessors.initColumn(o);
        return o;
    }
}
