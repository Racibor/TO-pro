package mock;

import mock.arguments.matcher.ArgumentsPredicate;
import mock.methods.MockedExpression;
import mock.methods.MockedMetadata;
import mock.verification.DefaultVerificationStrategy;
import mock.verification.VerificationStrategy;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InvocationDetails<T> {
    private final ObjenesisStd objenesis = new ObjenesisStd();

    private final CallMetadata callMetadata;

    private final List<CallMetadata> previousCalls;

    public List<CallMetadata> getPreviousCalls() {
        return previousCalls;
    }

    private final Map<ArgumentsPredicate, MockedExpression<T>> expressions;

    private final MockedExpression<T> defaultMethod;

    private ArgumentsPredicate lastPredicate = null;

    public VerificationStrategy getVerificationStrategy() {
        return verificationStrategy;
    }

    private VerificationStrategy verificationStrategy = new DefaultVerificationStrategy();

    private final AtomicInteger invokationCount;

    private boolean whenCalled = false;

    public InvocationDetails(CallMetadata callMetadata, Method defaultMethod) {
        this.callMetadata = callMetadata;
        this.defaultMethod = metadata -> (T) defaultMethod.invoke(metadata.getMock(), metadata.getArguments());
        invokationCount = new AtomicInteger(0);
        previousCalls = new ArrayList<>();
        expressions = new HashMap<>();
    }

    public void setLastPredicate(ArgumentsPredicate argumentsPredicate) {
        this.lastPredicate = argumentsPredicate;
    }

    public void setVerificationStrategy(VerificationStrategy verificationStrategy) {
        this.verificationStrategy = verificationStrategy;
    }

    public void thenReturn(T result) {
        if (lastPredicate == null) {
            throw new RuntimeException();
        }
        expressions.put(lastPredicate, metadata -> result);
    }

    public void thenAnswer(MockedExpression<T> function) {
        if (lastPredicate == null) {
            throw new RuntimeException();
        }
        expressions.put(lastPredicate, function);
    }

    public void thenThrow(Class<? extends Throwable> throwable) {
        if (lastPredicate == null) {
            throw new RuntimeException();
        }
        expressions.put(lastPredicate, metadata -> {
            throw objenesis.newInstance(throwable);
        });
    }

    public T getResult(CallMetadata callMetadata, boolean register) throws Throwable {
        if (register) {
            previousCalls.add(callMetadata);
        }
        verificationStrategy.verify(this);
        invokationCount.incrementAndGet();

        MockedMetadata mockedMethod = new MockedMetadata(callMetadata);
        return expressions.keySet().stream()
                .filter(predicate -> predicate.test(callMetadata))
                .map(expressions::get)
                .findFirst()
                .orElse(defaultMethod)
                .apply(mockedMethod);
    }

    public boolean test(CallMetadata callMetadata) {
        if (!this.callMetadata.getClassName().equals(callMetadata.getClassName())) {
            return false;
        }
        if (!this.callMetadata.getMethodName().equals(callMetadata.getMethodName())) {
            return false;
        }
        return Arrays.equals(this.callMetadata.getArgumentTypes(), callMetadata.getArgumentTypes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvocationDetails<?> that = (InvocationDetails<?>) o;
        return Objects.equals(objenesis, that.objenesis) && Objects.equals(callMetadata, that.callMetadata) && Objects.equals(previousCalls, that.previousCalls) && Objects.equals(expressions, that.expressions) && Objects.equals(lastPredicate, that.lastPredicate) && Objects.equals(verificationStrategy, that.verificationStrategy) && Objects.equals(invokationCount, that.invokationCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objenesis, callMetadata, previousCalls, expressions, lastPredicate, verificationStrategy, invokationCount);
    }
}
