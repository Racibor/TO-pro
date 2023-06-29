package mock.verification;

import mock.CallMetadata;
import mock.InvocationDetails;

import java.util.List;

public class InvocationAtLeastFilteredVerificationStrategy implements FilteredVerificationStrategy {

    private final int tries;

    public InvocationAtLeastFilteredVerificationStrategy(int tries) {
        this.tries = tries;
    }

    @Override
    public void verify(List<CallMetadata> invocationsList) {
        if (invocationsList.size() < tries) {
            throw new RuntimeException();
        }
    }
}