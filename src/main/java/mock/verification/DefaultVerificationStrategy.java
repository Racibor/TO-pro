package mock.verification;

import mock.InvocationDetails;

public class DefaultVerificationStrategy implements VerificationStrategy {
    @Override
    public void verify(InvocationDetails invocationDetails) {
        return;
    }
}
