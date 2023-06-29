package mock.arguments.captor;

import mock.Mocker;
import mock.MockingProgress;
import mock.arguments.matcher.predicates.ArgumentsAnyPredicate;

import java.util.List;

public class ArgumentCaptor<T> {
    private final Captor<T> captor = new Captor(new ArgumentsAnyPredicate());

    public T capture() {
        Mocker.getMockingProgress().registerMatcher(captor);
        return null;
    }

    public T getLastValue() {
        return captor.getLast();
    }

    public List<T> getValues() {
        return captor.getAllValues();
    }

}
