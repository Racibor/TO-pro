package mock;

public class DoReturnWhenDecorator {

    public <T> T when(T mock) {
        return mock;
    }
}
