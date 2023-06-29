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

    private final Map<ArgumentsPredicate, Queue<MockedExpression<T>>> expressions;

    private final MockedExpression<T> defaultMethod;

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

    public void manualIncrementInvocationCount() {
        invokationCount.incrementAndGet();
    }

    public void manualDecrementInvocationCount() {
        invokationCount.getAndDecrement();
    }

    public void setLastPredicate(ArgumentsPredicate argumentsPredicate) {
        //this.lastPredicate.set(argumentsPredicate);
    }

    public void setVerificationStrategy(VerificationStrategy verificationStrategy) {
        this.verificationStrategy = verificationStrategy;
    }

    public InvocationDetails<T> thenReturn(T result) {
        if (Mocker.getMockingProgress().getLastPredicate() == null) {
            throw new RuntimeException();
        }
        getAnswerQueue(Mocker.getMockingProgress().getLastPredicate()).offer(metadata -> result);
        return this;
    }

    public InvocationDetails<T> thenAnswer(MockedExpression<T> function) {
        if (Mocker.getMockingProgress().getLastPredicate() == null) {
            throw new RuntimeException();
        }
        getAnswerQueue(Mocker.getMockingProgress().getLastPredicate()).offer(function);
        return this;
    }

    public InvocationDetails<T> thenThrow(Class<? extends Throwable> throwable) {
        if (Mocker.getMockingProgress().getLastPredicate() == null) {
            throw new RuntimeException();
        }
        getAnswerQueue(Mocker.getMockingProgress().getLastPredicate()).offer(metadata -> {
            throw objenesis.newInstance(throwable);
        });
        return this;
    }

    private Queue<MockedExpression<T>> getAnswerQueue(ArgumentsPredicate argumentsPredicate) {
        ArgumentsPredicate basePredicate = expressions.keySet()
                .stream()
                .filter(predicate -> predicate.equals(argumentsPredicate))
                .findFirst()
                .orElse(argumentsPredicate);

        return expressions.computeIfAbsent(basePredicate, predicate -> new LinkedList<>());
    }

    public T getResult(CallMetadata callMetadata, boolean register) throws Throwable {
        if (register && !Mocker.getMockingProgress().veryfying) {
            previousCalls.add(callMetadata);
        }
        verificationStrategy.verify(this);
        this.verificationStrategy = x -> {};
        invokationCount.incrementAndGet();

        Mocker.getMockingProgress().veryfyingMulti = false;
        Mocker.getMockingProgress().veryfying = false;

        MockedMetadata mockedMethod = new MockedMetadata(callMetadata);
        Queue<MockedExpression<T>> expressionQueue = expressions.keySet().stream()
                .filter(predicate -> predicate.test(callMetadata))
                .map(expressions::get)
                .findFirst()
                .orElse(new LinkedList<>());

        if (expressionQueue.isEmpty()) {
            return defaultMethod.apply(mockedMethod);
        } else {
            return expressionQueue.size() == 1
                    ? expressionQueue.peek().apply(mockedMethod)
                    : expressionQueue.poll().apply(mockedMethod);
        }
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
        return Objects.equals(objenesis, that.objenesis) && Objects.equals(callMetadata, that.callMetadata) && Objects.equals(previousCalls, that.previousCalls) && Objects.equals(expressions, that.expressions) && Objects.equals(verificationStrategy, that.verificationStrategy) && Objects.equals(invokationCount, that.invokationCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objenesis, callMetadata, previousCalls, expressions, verificationStrategy, invokationCount);
    }
}
