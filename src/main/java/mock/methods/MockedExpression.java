package mock.methods;

import java.util.function.Function;

@FunctionalInterface
public interface MockedExpression<T> {

    T apply(MockedMetadata metadata) throws Throwable;
}
