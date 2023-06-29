package mock.verification;

import mock.CallMetadata;
import mock.InvocationDetails;

import java.util.List;

public interface FilteredVerificationStrategy {

    void verify(List<CallMetadata> invocationsList);

}
