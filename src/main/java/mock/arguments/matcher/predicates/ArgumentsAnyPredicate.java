package mock.arguments.matcher.predicates;

import java.util.Objects;
import java.util.function.Predicate;

public class ArgumentsAnyPredicate<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

}
