package mock;

import java.util.Arrays;
import java.util.Objects;

public class CallMetadata {
    private final String className;

    private final String methodName;

    private final Object[] arguments;

    public CallMetadata(String className, String methodName, Object[] arguments) {
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallMetadata that = (CallMetadata) o;
        return Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(className, methodName);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }
}
