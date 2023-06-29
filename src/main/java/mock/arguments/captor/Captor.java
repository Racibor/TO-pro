package mock.arguments.captor;

import mock.Mocker;
import mock.arguments.matcher.predicates.ArgumentsAnyPredicate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Captor<T> implements Predicate {

    private final LinkedList<T> capturedArguments = new LinkedList<>();

    private final Predicate<T> matcher;

    public Captor(Predicate<T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean test(Object o) {
        capturedArguments.add((T) o);
        return matcher.test((T) o);
    }

    public T getLast() {
        return capturedArguments.getLast();
    }

    public List<T> getAllValues() {
        return Collections.unmodifiableList(capturedArguments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null) {
            if (o instanceof ArgumentsAnyPredicate) {
                return true;
            }
            return getClass() == o.getClass();
        }
        return false;
    }
}
