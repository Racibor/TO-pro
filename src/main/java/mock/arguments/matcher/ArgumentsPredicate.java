package mock.arguments.matcher;

import mock.CallMetadata;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ArgumentsPredicate implements Predicate<CallMetadata> {

    private final List<Predicate> predicateList;


    public ArgumentsPredicate(List<Predicate> predicateList) {
        this.predicateList = predicateList;
    }

    @Override
    public boolean test(CallMetadata callMetadata) {
        if (predicateList.size() > callMetadata.getArguments().length) {
            return false;
        }

        Object[] arguments = callMetadata.getArguments();

        for (int i = 0; i < predicateList.size(); i++) {
            Predicate predicate = predicateList.get(i);
            if (!predicate.test(arguments[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArgumentsPredicate that = (ArgumentsPredicate) o;
        return Objects.equals(predicateList, that.predicateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicateList);
    }
}
