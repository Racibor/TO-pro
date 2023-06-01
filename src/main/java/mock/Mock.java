package mock;

import mock.mem.CopyUtil;
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

    public static <T> T verify(T mock, VerificationStrategy verificationStrategy) {
        return mocker.verify(mock, verificationStrategy);
    }

    public static <T> T spy(T object) {
        T mock = (T) mocker.spy(object.getClass());
        CopyUtil.copy(object, mock, object.getClass());
        return mock;
    }
}
