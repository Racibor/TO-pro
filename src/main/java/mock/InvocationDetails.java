package mock;

import mock.arguments.matcher.ArgumentsPredicate;
import mock.methods.MockedExpression;
import mock.methods.MockedMetadata;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Function;

public class InvocationDetails<T> {
    private final ObjenesisStd objenesis = new ObjenesisStd();

    private final CallMetadata callMetadata;

    private final ArgumentsPredicate argumentsPredicate;

    private MockedExpression<T> result;

    public InvocationDetails(CallMetadata callMetadata, ArgumentsPredicate argumentsPredicate) {
        this.callMetadata = callMetadata;
        this.argumentsPredicate = argumentsPredicate;
    }


    public void thenReturn(T result) {
        this.result = metadata -> result;
    }

    public void thenAnswer(MockedExpression<T> function) {
        this.result = function;
    }

    public void thenThrow(Class<? extends Throwable> throwable) {
        this.result = metadata -> {
            throw objenesis.newInstance(throwable);
        };
    }

    public T getResult(CallMetadata callMetadata) throws Throwable{
        MockedMetadata mockedMethod = new MockedMetadata(callMetadata);
        return result.apply(mockedMethod);
    }

    public boolean test(CallMetadata callMetadata) {
        if (!this.callMetadata.getClassName().equals(callMetadata.getClassName())) {
            return false;
        }
        if (!this.callMetadata.getMethodName().equals(callMetadata.getMethodName())) {
            return false;
        }
        return argumentsPredicate.test(callMetadata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvocationDetails<?> that = (InvocationDetails<?>) o;
        return Objects.equals(callMetadata, that.callMetadata) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callMetadata, result);
    }
}
