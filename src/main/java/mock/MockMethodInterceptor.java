package mock;

import mock.arguments.matcher.ArgumentsPredicate;
import mock.arguments.matcher.Matcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MockMethodInterceptor {

    private final List<InvocationDetails> invocationDetailsList;

    public MockMethodInterceptor(List<InvocationDetails> callMetadataList) {
        this.invocationDetailsList = callMetadataList;
    }

    public Object invoke(Object mock, Method invokedMethod, Object[] arguments) {
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

        CallMetadata callMetadata = new CallMetadata(mock.getClass().getName(), methodName, arguments);
        Optional<InvocationDetails> invocation = searchFor(callMetadata);
        if (invocation.isPresent()) {
            return invocation.get().getResult(callMetadata);
        } else {
            InvocationDetails invocationDetails = new InvocationDetails(callMetadata, new ArgumentsPredicate(argumentPredicates));
            invocationDetailsList.add(invocationDetails);
            return invokedMethod.getDefaultValue();
        }
    }

    private Optional<InvocationDetails> searchFor(CallMetadata callMetadata) {
        return invocationDetailsList.stream().filter(invocation -> invocation.test(callMetadata)).findFirst();
    }
}
