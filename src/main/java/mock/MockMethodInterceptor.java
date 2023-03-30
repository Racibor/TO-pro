package mock;

import java.lang.reflect.Method;
import java.util.List;

public class MockMethodInterceptor {

    private final List<CallMetadata> callMetadataList;

    public MockMethodInterceptor(List<CallMetadata> callMetadataList) {
        this.callMetadataList = callMetadataList;
    }

    public Object invoke(Object mock, Method invokedMethod, Object[] arguments) {
        String methodName = invokedMethod.getName();

        CallMetadata callMetadata = new CallMetadata(mock.getClass().getName(), methodName, arguments);

        if(!callMetadataList.contains(callMetadata)) {
            callMetadataList.add(callMetadata);
            return invokedMethod.getDefaultValue();
        } else {
            CallMetadata recorded = callMetadataList.get(callMetadataList.indexOf(callMetadata));
            return recorded.getResult();
        }
    }
}
