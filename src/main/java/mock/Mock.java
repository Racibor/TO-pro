package mock;

import mock.mem.CopyUtil;
import mock.methods.MockedExpression;
import mock.verification.FilteredVerificationStrategy;
import mock.verification.VerificationStrategy;

public class Mock {

    static final Mocker mocker = new Mocker();

    public static <T> T mock(Class<T> t) {
        return mocker.mock(t);
    }

    public static <T> InvocationDetails<T> when(T methodCall) {
        return mocker.when(methodCall);
    }

    public static <T> T verify(T mock) {
        return mocker.verify(mock);
    }

    public static <T> T verify(T mock, FilteredVerificationStrategy verificationStrategy) {
        return mocker.verify(mock, verificationStrategy);
    }

    public static DoReturnWhenDecorator doReturn(Object toReturn) {
        return mocker.doReturn(toReturn);
    }

    public static DoReturnWhenDecorator doAnswer(MockedExpression toReturn) {
        return mocker.doAnswer(toReturn);
    }

    public static DoReturnWhenDecorator doThrow(Class<? extends Throwable> throwable) {
        return mocker.doThrow(throwable);
    }

    public static <T> T spy(T object) {
        T mock = (T) mocker.spy(object.getClass());
        CopyUtil.copy(object, mock, object.getClass());
        return mock;
    }
}
