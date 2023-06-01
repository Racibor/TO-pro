package mock;

import mock.verification.FilteredVerificationStrategy;
import mock.verification.InvokationTimesVerificationStrategy;
import mock.verification.VerificationStrategy;

import java.util.ArrayList;
import java.util.List;

public class Mocker {

    private final List<InvocationDetails> callList = new ArrayList<>();

    private static final ThreadLocal<MockingProgress> mockingProgress = new ThreadLocal<>() {
        @Override
        protected MockingProgress initialValue() {
            return new MockingProgress();
        }
    };
    private final MockFactory mockFactory = new MockFactory();

    public <T> T mock(Class<T> t) {
        T result = mockFactory.createMock(t, callList);
        return result;
    }

    public <T> T spy(Class<T> t) {
        T result = mockFactory.createSpy(t, callList);
        return result;
    }

    public <T> InvocationDetails<T> when(T methodCall) {
        MockContainer.register(MockingProgress.getLastMockHash(), MockingProgress.getLastInvocation());
        return MockingProgress.getLastInvocation();
    }

    public <T> T verify(T mock) {
        MockingProgress.veryfying = true;
        return mock;
    }

    public <T> T verify(T mock, FilteredVerificationStrategy verificationStrategy) {
        MockingProgress.veryfying = true;
        MockingProgress.veryfyingMulti = true;
        InvocationDetails invocationDetails = MockingProgress.getLastInvocation();
        MockingProgress.setFilteredVerificationStrategy(verificationStrategy);
        return mock;
    }

}
