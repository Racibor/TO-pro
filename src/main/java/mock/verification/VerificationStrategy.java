package mock.verification;

import mock.InvocationDetails;

public interface VerificationStrategy {

    void verify(InvocationDetails invocationDetails);
}
