package mock;

import mock.arguments.matcher.ArgumentsPredicate;
import mock.methods.MockedExpression;
import mock.verification.FilteredVerificationStrategy;
import mock.verification.VerificationStrategy;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public class MockingProgress {

    public boolean veryfying = false;

    public boolean veryfyingMulti = false;

    public boolean whenCalled = false;

    private Queue<Predicate> matchers = new ConcurrentLinkedQueue<>();

    private String lastMockHash;

    private InvocationDetails lastInvocation;

    private ArgumentsPredicate lastPredicate;

    private VerificationStrategy verificationStrategy;

    private FilteredVerificationStrategy filteredVerificationStrategy;

    public boolean doReturnCalled = false;

    private MockedExpression toReturn;

    public void registerMatcher(Predicate argumentsPredicate) {
        this.matchers.offer(argumentsPredicate);
    }

    public Predicate poolMatcher() {
        return this.matchers.poll();
    }

    public void setLastInvocation(InvocationDetails invocationDetails) {
        this.lastInvocation = invocationDetails;
    }

    public InvocationDetails getLastInvocation() {
        return this.lastInvocation;
    }

    public String getLastMockHash() {
        return lastMockHash;
    }

    public FilteredVerificationStrategy getFilteredVerificationStrategy() {
        return filteredVerificationStrategy;
    }

    public ArgumentsPredicate getLastPredicate() {
        return lastPredicate;
    }

    public void setLastPredicate(ArgumentsPredicate lastPredicate) {
        this.lastPredicate = lastPredicate;
    }

    public void setToReturn(MockedExpression toReturn) {
        this.toReturn = toReturn;
    }

    public MockedExpression getToReturn() {
        return toReturn;
    }

    public void setFilteredVerificationStrategy(FilteredVerificationStrategy filteredVerificationStrategy) {
        this.filteredVerificationStrategy = filteredVerificationStrategy;
    }

    public void setLastMockHash(String lastMockHash) {
        this.lastMockHash = lastMockHash;
    }

    public VerificationStrategy getVerificationStrategy() {
        return this.verificationStrategy;
    }

    public void setVerificationStrategy(VerificationStrategy verificationStrategy) {
        this.verificationStrategy = verificationStrategy;
    }
}