import java.util.List;
import java.util.function.Consumer;

public class TestContainer<T> {

    private final List<T> list;


    public TestContainer(List<T> list) {
        this.list = list;
    }

    public boolean add(T obj) {
        return list.add(obj);
    }

    public void remove(int index) {
        list.remove(index);
    }

    public void forEach(Consumer<T> consumer) {
        list.forEach(consumer);
    }
}
