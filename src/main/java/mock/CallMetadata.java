package mock;

import java.util.Arrays;
import java.util.Objects;

public class CallMetadata<T> {

    private String className;

    private String methodName;

    private Object[] arguments;

    private T result;

    public CallMetadata(String className, String methodName, Object[] arguments) {
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public void thenReturn(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallMetadata<?> that = (CallMetadata<?>) o;
        return Objects.equals(className, that.className)
                && Objects.equals(methodName, that.methodName)
                && Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hash(className, methodName, result);
        result1 = 31 * result1 + Arrays.hashCode(arguments);
        return result1;
    }
}
