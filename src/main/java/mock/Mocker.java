package mock;

import mock.methods.MockedExpression;
import mock.verification.FilteredVerificationStrategy;
import org.objenesis.ObjenesisStd;

import java.util.ArrayList;
import java.util.List;

public class Mocker {

    private final ObjenesisStd objenesis = new ObjenesisStd();

    private final List<InvocationDetails> callList = new ArrayList<>();

    private static ThreadLocal<MockingProgress> mockingProgress = new ThreadLocal<>() {
        @Override
        protected MockingProgress initialValue() {
            return null;
        }
    };

    public static MockingProgress getMockingProgress() {
        if (mockingProgress.get() == null) {
            mockingProgress.set(new MockingProgress());
        }
        return mockingProgress.get();
    }
    private final MockFactory mockFactory = new MockFactory();

    public <T> T mock(Class<T> t) {
        T result = mockFactory.createMock(t, callList);
        return result;
    }

    public <T> T spy(Class<T> t) {
        T result = mockFactory.createSpy(t, callList);
        return result;
    }

    public DoReturnWhenDecorator doReturn(Object toReturn) {
        Mocker.getMockingProgress().setToReturn(x -> toReturn);
        Mocker.getMockingProgress().doReturnCalled = true;
        return new DoReturnWhenDecorator();
    }

    public DoReturnWhenDecorator doAnswer(MockedExpression toReturn) {
        Mocker.getMockingProgress().setToReturn(toReturn);
        Mocker.getMockingProgress().doReturnCalled = true;
        return new DoReturnWhenDecorator();
    }

    public DoReturnWhenDecorator doThrow(Class<? extends Throwable> throwable) {
        Mocker.getMockingProgress().setToReturn(x -> {
            throw objenesis.newInstance(throwable);
        });
        Mocker.getMockingProgress().doReturnCalled = true;
        return new DoReturnWhenDecorator();
    }

    public <T> InvocationDetails<T> when(T methodCall) {
        MockContainer.register(Mocker.getMockingProgress().getLastMockHash(), Mocker.getMockingProgress().getLastInvocation());
        Mocker.getMockingProgress().getLastInvocation().manualDecrementInvocationCount();
        return Mocker.getMockingProgress().getLastInvocation();
    }

    public <T> T verify(T mock) {
        Mocker.getMockingProgress().veryfying = true;
        return mock;
    }

    public <T> T verify(T mock, FilteredVerificationStrategy verificationStrategy) {
        Mocker.getMockingProgress().veryfying = true;
        Mocker.getMockingProgress().veryfyingMulti = true;
        Mocker.getMockingProgress().setFilteredVerificationStrategy(verificationStrategy);
        return mock;
    }

}
