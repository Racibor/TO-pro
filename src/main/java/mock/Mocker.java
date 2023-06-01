package mock;

import mock.verification.InvokationTimesVerificationStrategy;
import mock.verification.VerificationStrategy;

import java.util.ArrayList;
import java.util.List;

public class Mocker {

    private final List<InvocationDetails> callList = new ArrayList<>();

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
        return callList.get(callList.size() - 1);
    }

    public <T> T verify(T mock) {
        MockMethodInterceptor.veryfying = true;
        //return callList.get(callList.size() - 1);
        return mock;
    }

    public <T> T verify(T mock, VerificationStrategy verificationStrategy) {
        MockMethodInterceptor.veryfying = true;
        InvocationDetails invocationDetails = callList.get(callList.size() - 1);
        invocationDetails.setVerificationStrategy(verificationStrategy);
        return mock;
    }

}
