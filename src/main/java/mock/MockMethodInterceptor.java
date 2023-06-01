package mock;

import mock.arguments.matcher.ArgumentsPredicate;
import mock.arguments.matcher.Matcher;
import mock.verification.InvokationArgumentsVerificationStrategy;
import mock.verification.InvokationArgumentsVerificationStrategyMulti;
import mock.verification.InvokationArgumentsVerificationStrategySingle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MockMethodInterceptor {

    private final List<InvocationDetails> invocationDetailsList;

    private final boolean spy;

    private final String mockHash;

    public MockMethodInterceptor(List<InvocationDetails> callMetadataList, boolean spy, String mockHash) {
        this.invocationDetailsList = callMetadataList;
        this.spy = spy;
        this.mockHash = mockHash;
    }

    public Object invoke(Object mock, Method invokedMethod, Object[] arguments, Method superMethod) throws Throwable {
        List<Predicate> argumentPredicates = new ArrayList<>();
        for (Object arg : arguments) {
            if (arg == null || arg == "") {
                Predicate predicate = MockingProgress.poolMatcher();
                if (predicate == null) {
                    Matcher.any();
                    argumentPredicates.add(MockingProgress.poolMatcher());
                } else {
                    argumentPredicates.add(predicate);
                }
            } else {
                Matcher.eq(arg);
                argumentPredicates.add(MockingProgress.poolMatcher());
            }
        }

        String methodName = invokedMethod.getName();

        if (arguments.length != invokedMethod.getParameterTypes().length) {
            throw new RuntimeException("");
        }
        CallMetadata callMetadata = new CallMetadata(mock.getClass().getName(), methodName, arguments, invokedMethod.getParameterTypes(), mock);
        Optional<InvocationDetails> invocation = searchFor(callMetadata);
        if (invocation.isPresent()) {
            InvocationDetails invocationDetails = invocation.get();
            MockingProgress.setLastInvocation(invocationDetails);
            MockingProgress.setLastMockHash(mockHash);
            invocationDetails.setLastPredicate(new ArgumentsPredicate(argumentPredicates));
            if (MockingProgress.veryfying) {
                if (MockingProgress.veryfyingMulti) {
                    invocationDetails.setVerificationStrategy(new InvokationArgumentsVerificationStrategyMulti(new ArgumentsPredicate(argumentPredicates), MockingProgress.getFilteredVerificationStrategy()));
                } else {
                    invocationDetails.setVerificationStrategy(new InvokationArgumentsVerificationStrategySingle(new ArgumentsPredicate(argumentPredicates)));
                }
            }
            return invocationDetails.getResult(callMetadata, true);
        } else {
            InvocationDetails invocationDetails = new InvocationDetails(callMetadata, superMethod);
            invocationDetails.setLastPredicate(new ArgumentsPredicate(argumentPredicates));
            MockingProgress.setLastInvocation(invocationDetails);
            MockingProgress.setLastMockHash(mockHash);

            if (!spy) {
                if (!invokedMethod.getReturnType().isPrimitive()) {
                    return invokedMethod.getDefaultValue();
                } else {
                    return false;
                }
            } else {
              return invocationDetails.getResult(callMetadata, false);
            }
        }
    }

    private Optional<InvocationDetails> searchFor(CallMetadata callMetadata) {
        return MockContainer.getMocks(mockHash).stream().filter(invocation -> invocation.test(callMetadata)).findFirst();
    }
}
