package mock;

import java.util.ArrayList;
import java.util.List;

public class Mocker {

    private final List<CallMetadata> callList = new ArrayList<>();

    private final MockFactory mockFactory = new MockFactory();

    public <T> T mock(Class<T> t) {
        T result = mockFactory.createMock(t, callList);
        return result;
    }

    public <T> CallMetadata<T> when(T methodCall) {
        return callList.get(callList.size() - 1);
    }

}
