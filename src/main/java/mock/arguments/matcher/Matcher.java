package mock.arguments.matcher;

import mock.MockingProgress;
import mock.arguments.matcher.predicates.ArgumentsAnyPredicate;
import mock.arguments.matcher.predicates.ArgumentsEqualPredicate;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public class Matcher {

    public static <T> T eq(T obj) {
        MockingProgress.registerMatcher(new ArgumentsEqualPredicate<T>(obj));
        return null;
    }

    public static <T> T any() {
        MockingProgress.registerMatcher(new ArgumentsAnyPredicate<T>());
        return null;
    }

}
