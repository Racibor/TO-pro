package mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MockContainer {

    public static final ConcurrentHashMap<String, List<InvocationDetails>> mockContainer = new ConcurrentHashMap();

    public static synchronized InvocationDetails register(String mockHash, InvocationDetails invocationDetails) {
        List<InvocationDetails> mockList = mockContainer.getOrDefault(mockHash, new ArrayList<>());
        if (mockList.contains(invocationDetails)) {
            return invocationDetails;
        }
        mockList.add(invocationDetails);
        mockContainer.put(mockHash, mockList);
        return invocationDetails;
    }

    public static List<InvocationDetails> getMocks(String mockHash) {
        return mockContainer.getOrDefault(mockHash, new ArrayList<>());
    }

}
