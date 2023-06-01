package mock;

import mock.arguments.matcher.ArgumentsPredicate;
import mock.arguments.matcher.Matcher;
import mock.verification.InvokationArgumentsVerificationStrategy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MockMethodInterceptor {

    public static boolean veryfying = false;

    private final List<InvocationDetails> invocationDetailsList;

    private final boolean spy;

    public MockMethodInterceptor(List<InvocationDetails> callMetadataList, boolean spy) {
        this.invocationDetailsList = callMetadataList;
        this.spy = spy;
    }

    public Object invoke(Object mock, Method invokedMethod, Object[] arguments, Method superMethod) throws Throwable {
        List<Predicate> argumentPredicates = new ArrayList<>();
        for (Object arg : arguments) {
            if (arg == null || arg == "") {
                Predicate predicate = Matcher.poll();
                if (predicate == null) {
                    Matcher.any();
                    argumentPredicates.add(Matcher.poll());
                } else {
                    argumentPredicates.add(predicate);
                }
            } else {
                Matcher.eq(arg);
                argumentPredicates.add(Matcher.poll());
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
            invocationDetails.setLastPredicate(new ArgumentsPredicate(argumentPredicates));
            boolean registerCall = !veryfying;
            if (veryfying) {
                veryfying = false;
                invocationDetails.setVerificationStrategy(new InvokationArgumentsVerificationStrategy(new ArgumentsPredicate(argumentPredicates), invocationDetails.getVerificationStrategy()));
            }
            return invocationDetails.getResult(callMetadata, registerCall);
        } else {
            InvocationDetails invocationDetails = new InvocationDetails(callMetadata, superMethod);
            invocationDetails.setLastPredicate(new ArgumentsPredicate(argumentPredicates));
            invocationDetailsList.add(invocationDetails);

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
        return invocationDetailsList.stream().filter(invocation -> invocation.test(callMetadata)).findFirst();
    }
}
