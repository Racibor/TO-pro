package mock.arguments.matcher;

import mock.arguments.matcher.predicates.ArgumentsAnyPredicate;
import mock.arguments.matcher.predicates.ArgumentsEqualPredicate;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public class Matcher {
    private static Queue<Predicate> session = new ConcurrentLinkedQueue<>();

    public static <T> T eq(T obj) {
        session.offer(new ArgumentsEqualPredicate<T>(obj));
        return null;
    }

    public static <T> T any() {
        session.offer(new ArgumentsAnyPredicate<T>());
        return null;
    }

    public static Predicate poll() {
        return session.poll();
    }

}
