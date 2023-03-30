package mock;

import java.util.ArrayList;
import java.util.List;

public class Mock {

    static final Mocker mocker = new Mocker();

    public static <T> T mock(Class<T> t) {
        return mocker.mock(t);
    }

    public static <T> CallMetadata<T> when(T methodCall) {
        return mocker.when(methodCall);
    }
}
