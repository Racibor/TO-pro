package mock.methods;

import mock.CallMetadata;

public class MockedMetadata {

    private final CallMetadata callMetadata;

    private final Object mock;

    public MockedMetadata(CallMetadata callMetadata) {
        this.callMetadata = callMetadata;
        this.mock = callMetadata.getMock();
    }

    public Object[] getArguments() {
        return callMetadata.getArguments();
    }

    public Object getMock() {
        return mock;
    }
}
