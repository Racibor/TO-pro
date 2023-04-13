package mock.arguments.matcher.predicates;

import java.util.function.Predicate;

public class ArgumentsEqualPredicate<T> implements Predicate<T> {

    private final T object;

    public ArgumentsEqualPredicate(T object) {
        this.object = object;
    }

    @Override
    public boolean test(Object o) {
        return object.equals(o);
    }
}
