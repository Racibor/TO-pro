package mock;

public class Mock {

    static final Mocker mocker = new Mocker();

    public static <T> T mock(Class<T> t) {
        return mocker.mock(t);
    }

    public static <T> InvocationDetails<T> when(T methodCall) {
        return mocker.when(methodCall);
    }
}
