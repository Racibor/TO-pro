package mock.verification;

import mock.CallMetadata;
import mock.InvocationDetails;
import mock.arguments.matcher.ArgumentsPredicate;

public class InvokationArgumentsVerificationStrategySingle implements VerificationStrategy {

    private final ArgumentsPredicate argumentsPredicate;

    public InvokationArgumentsVerificationStrategySingle(ArgumentsPredicate argumentsPredicate) {
        this.argumentsPredicate = argumentsPredicate;
    }

    @Override
    public void verify(InvocationDetails invocationDetails) {
        if (invocationDetails.getPreviousCalls().isEmpty()) {
            throw new RuntimeException();
        }
        if (!argumentsPredicate.test((CallMetadata) invocationDetails.getPreviousCalls().get(invocationDetails.getPreviousCalls().size() - 1))) {
            throw new RuntimeException();
        }
    }
}
