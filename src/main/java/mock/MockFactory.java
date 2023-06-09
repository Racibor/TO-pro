package mock;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import org.objenesis.ObjenesisStd;

import java.util.Date;
import java.util.List;

import static net.bytebuddy.description.modifier.Visibility.PRIVATE;
import static net.bytebuddy.matcher.ElementMatchers.any;

public class MockFactory {

    private final ObjenesisStd objenesis = new ObjenesisStd();

    private final ByteBuddy byteBuddy = new ByteBuddy();

    public <T> T createMock(Class<T> t, List<InvocationDetails> callList) {

        Class<? extends T> classWithInterceptor = byteBuddy.subclass(t)
                .method(any())
                .intercept(MethodDelegation.to(InterceptorDelegate.class))
                .defineField("interceptor", MockMethodInterceptor.class, PRIVATE)
                .implement(Interceptable.class)
                .intercept(FieldAccessor.ofBeanProperty())
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();

        String mockHash = t.getName() + new Date().getTime();

        T instance = objenesis.newInstance(classWithInterceptor);//(T) classWithInterceptor.getClass().getConstructor().newInstance();
        ((Interceptable) instance).setInterceptor(new MockMethodInterceptor(callList, false, mockHash));

        return instance;
    }

    public <T> T createSpy(Class<T> t, List<InvocationDetails> callList) {

        Class<? extends T> classWithInterceptor = byteBuddy.subclass(t)
                .method(any())
                .intercept(MethodDelegation.to(InterceptorDelegate.class))
                .defineField("interceptor", MockMethodInterceptor.class, PRIVATE)
                .implement(Interceptable.class)
                .intercept(FieldAccessor.ofBeanProperty())
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();

        String mockHash = t.getName() + new Date().getTime();

        T instance = objenesis.newInstance(classWithInterceptor);//(T) classWithInterceptor.getClass().getConstructor().newInstance();
        ((Interceptable) instance).setInterceptor(new MockMethodInterceptor(callList, true, mockHash));

        return instance;
    }

}
