package mock.arguments.matcher.predicates;

import java.util.function.Predicate;

public class ArgumentsAnyPredicate<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return true;
    }
}
