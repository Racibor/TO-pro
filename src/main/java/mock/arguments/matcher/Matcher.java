package mock.arguments.matcher;

import mock.Mocker;
import mock.MockingProgress;
import mock.Primitives;
import mock.arguments.matcher.predicates.ArgumentsAnyPredicate;
import mock.arguments.matcher.predicates.ArgumentsEqualPredicate;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public class Matcher {

    public static <T> T eq(T obj) {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsEqualPredicate<T>(obj));
        return null;
    }

    public static int eq(int obj) {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsEqualPredicate<>(obj));
        return 0;
    }

    public static long eq(long obj) {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsEqualPredicate<>(obj));
        return 0;
    }

    public static double eq(double obj) {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsEqualPredicate<>(obj));
        return 0;
    }

    public static float eq(float obj) {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsEqualPredicate<>(obj));
        return 0;
    }

    public static <T> T any() {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsAnyPredicate<T>());
        return null;
    }

    public static int anyInt() {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsAnyPredicate<>());
        return 0;
    }

    public float anyFloat() {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsAnyPredicate<>());
        return 0.0f;
    }

    public double anyDouble() {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsAnyPredicate<>());
        return 0.0;
    }

    public char anyChar() {
        Mocker.getMockingProgress().registerMatcher(new ArgumentsAnyPredicate<>());
        return 'a';
    }
}
