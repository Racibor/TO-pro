package mock;

import java.util.Arrays;
import java.util.Objects;

public class CallMetadata {
    private final String className;

    private final String methodName;

    private final Object[] arguments;

    private final Class[] argumentTypes;

    private final Object mock;

    public CallMetadata(String className, String methodName, Object[] arguments, Class[] argumentTypes, Object mock) {
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments;
        this.argumentTypes = argumentTypes;
        this.mock = mock;
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

    public Class[] getArgumentTypes() {
        return argumentTypes;
    }

    public Object getMock() {
        return mock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallMetadata that = (CallMetadata) o;
        return Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Arrays.equals(arguments, that.arguments) && Arrays.equals(argumentTypes, that.argumentTypes) && Objects.equals(mock, that.mock);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(className, methodName, mock);
        result = 31 * result + Arrays.hashCode(arguments);
        result = 31 * result + Arrays.hashCode(argumentTypes);
        return result;
    }
}
