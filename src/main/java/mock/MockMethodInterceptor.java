package mock;

import mock.arguments.matcher.ArgumentsPredicate;
import mock.arguments.matcher.predicates.ArgumentsAnyPredicate;
import mock.arguments.matcher.predicates.ArgumentsEqualPredicate;
import mock.verification.InvokationArgumentsVerificationStrategyMulti;
import mock.verification.InvokationArgumentsVerificationStrategySingle;
import mock.verification.VerificationStrategy;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Period;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
            if (arg == null || arg == "" || (Primitives.isPrimitiveOrWrapper(arg.getClass()) && Primitives.defaultValue(arg.getClass()).equals(arg))) {
                Predicate predicate = Mocker.getMockingProgress().poolMatcher();
                if (predicate == null) {
                    argumentPredicates.add(new ArgumentsAnyPredicate());
                } else {
                    argumentPredicates.add(predicate);
                }
            } else {
                argumentPredicates.add(new ArgumentsEqualPredicate(arg));
            }
        }
        Mocker.getMockingProgress().setLastPredicate(new ArgumentsPredicate(argumentPredicates));

        String methodName = invokedMethod.getName();

        if (arguments.length != invokedMethod.getParameterTypes().length) {
            throw new RuntimeException("");
        }
        CallMetadata callMetadata = new CallMetadata(mock.getClass().getName(), methodName, arguments, invokedMethod.getParameterTypes(), mock);
        Optional<InvocationDetails> invocation = searchFor(callMetadata);
        if (invocation.isPresent()) {
            InvocationDetails invocationDetails = invocation.get();
            Mocker.getMockingProgress().setLastInvocation(invocationDetails);
            Mocker.getMockingProgress().setLastMockHash(mockHash);
            invocationDetails.setLastPredicate(new ArgumentsPredicate(argumentPredicates));
            if (Mocker.getMockingProgress().veryfying) {
                VerificationStrategy verificationStrategy;
                if (Mocker.getMockingProgress().veryfyingMulti) {
                    verificationStrategy = new InvokationArgumentsVerificationStrategyMulti(new ArgumentsPredicate(argumentPredicates), Mocker.getMockingProgress().getFilteredVerificationStrategy());
                } else {
                    verificationStrategy = new InvokationArgumentsVerificationStrategySingle(new ArgumentsPredicate(argumentPredicates));
                }
                if (verificationStrategy != null) {
                    verificationStrategy.verify(invocationDetails);
                }
                Mocker.getMockingProgress().veryfying = false;
                Mocker.getMockingProgress().veryfyingMulti = false;
                return returnValueFor(invokedMethod.getReturnType());
            }
            if (Mocker.getMockingProgress().doReturnCalled) {
                Mocker.getMockingProgress().doReturnCalled = false;
                invocationDetails.thenAnswer(Mocker.getMockingProgress().getToReturn());
                return returnValueFor(invokedMethod.getReturnType());
            }
            return invocationDetails.getResult(callMetadata, true);
        } else {
            InvocationDetails invocationDetails = new InvocationDetails(callMetadata, superMethod);
            //invocationDetails.setLastPredicate(new ArgumentsPredicate(argumentPredicates));
            Mocker.getMockingProgress().setLastPredicate(new ArgumentsPredicate(argumentPredicates));
            Mocker.getMockingProgress().setLastInvocation(invocationDetails);
            Mocker.getMockingProgress().setLastMockHash(mockHash);
            MockContainer.register(Mocker.getMockingProgress().getLastMockHash(), Mocker.getMockingProgress().getLastInvocation());
            if (Mocker.getMockingProgress().doReturnCalled) {
                Mocker.getMockingProgress().doReturnCalled = false;
                invocationDetails.thenAnswer(Mocker.getMockingProgress().getToReturn());
                MockContainer.register(Mocker.getMockingProgress().getLastMockHash(), Mocker.getMockingProgress().getLastInvocation());
                return returnValueFor(invokedMethod.getReturnType());
            }
            if (!spy) {
                return invocationDetails.getResult(callMetadata, true);
            } else {
              return invocationDetails.getResult(callMetadata, false);
            }
        }
    }

    private Optional<InvocationDetails> searchFor(CallMetadata callMetadata) {
        return MockContainer.getMocks(mockHash).stream().filter(invocation -> invocation.test(callMetadata)).findFirst();
    }

    Object returnValueFor(Class<?> type) {
        if (Primitives.isPrimitiveOrWrapper(type)) {
            return Primitives.defaultValue(type);
        } else if (type == Iterable.class) {
            return new ArrayList(0);
        } else if (type == Collection.class) {
            return new LinkedList();
        } else if (type == Set.class) {
            return new HashSet();
        } else if (type == HashSet.class) {
            return new HashSet();
        } else if (type == SortedSet.class) {
            return new TreeSet();
        } else if (type == TreeSet.class) {
            return new TreeSet();
        } else if (type == LinkedHashSet.class) {
            return new LinkedHashSet();
        } else if (type == List.class) {
            return new LinkedList();
        } else if (type == LinkedList.class) {
            return new LinkedList();
        } else if (type == ArrayList.class) {
            return new ArrayList();
        } else if (type == Map.class) {
            return new HashMap();
        } else if (type == HashMap.class) {
            return new HashMap();
        } else if (type == SortedMap.class) {
            return new TreeMap();
        } else if (type == TreeMap.class) {
            return new TreeMap();
        } else if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        } else if (type == Optional.class) {
            return Optional.empty();
        } else if (type == OptionalDouble.class) {
            return OptionalDouble.empty();
        } else if (type == OptionalInt.class) {
            return OptionalInt.empty();
        } else if (type == OptionalLong.class) {
            return OptionalLong.empty();
        } else if (type == Stream.class) {
            return Stream.empty();
        } else if (type == DoubleStream.class) {
            return DoubleStream.empty();
        } else if (type == IntStream.class) {
            return IntStream.empty();
        } else if (type == LongStream.class) {
            return LongStream.empty();
        } else if (type == Duration.class) {
            return Duration.ZERO;
        } else {
            return type == Period.class ? Period.ZERO : null;
        }
    }
}
