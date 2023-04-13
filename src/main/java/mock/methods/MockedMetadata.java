package mock.methods;

import mock.CallMetadata;

public class MockedMetadata {

    private final CallMetadata callMetadata;

    public MockedMetadata(CallMetadata callMetadata) {
        this.callMetadata = callMetadata;
    }

    public Object[] getArguments() {
        return callMetadata.getArguments();
    }


}
