package mock;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

public class InterceptorDelegate {
    @RuntimeType
    public static Object intercept(@This Object mock,
                                   @FieldValue("interceptor") MockMethodInterceptor interceptor,
                                   @Origin Method invokedMethod,
                                   @AllArguments Object[] arguments,
                                   @SuperMethod Method superMethod) throws Throwable {

        return interceptor.invoke(mock, invokedMethod, arguments, superMethod);
    }

}
