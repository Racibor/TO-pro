package mock.arguments.matcher.predicates;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArgumentsEqualPredicate<?> that = (ArgumentsEqualPredicate<?>) o;
        return Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }
}
