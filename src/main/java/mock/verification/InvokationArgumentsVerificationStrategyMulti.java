package mock.verification;

import mock.CallMetadata;
import mock.InvocationDetails;
import mock.arguments.matcher.ArgumentsPredicate;

import java.util.List;
import java.util.stream.Collectors;

public class InvokationArgumentsVerificationStrategyMulti implements VerificationStrategy {

    private final ArgumentsPredicate argumentsPredicate;

    private final FilteredVerificationStrategy verificationStrategy;

    public InvokationArgumentsVerificationStrategyMulti(ArgumentsPredicate argumentsPredicate, FilteredVerificationStrategy verificationStrategy) {
        this.argumentsPredicate = argumentsPredicate;
        this.verificationStrategy = verificationStrategy;
    }

    @Override
    public void verify(InvocationDetails invocationDetails) {
        List<CallMetadata> metadataList = (List<CallMetadata>) invocationDetails.getPreviousCalls().stream()
                .filter(call -> argumentsPredicate.test((CallMetadata) call))
                .collect(Collectors.toList());
        verificationStrategy.verify(metadataList);
    }
}
