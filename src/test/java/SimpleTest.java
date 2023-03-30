import mock.Mock;
import org.junit.jupiter.api.Test;

public class SimpleTest {

    @Test
    public void simple() {
        Car car = Mock.mock(Car.class);
        Mock.when(car.wrum()).thenReturn("test");
        System.out.println(car.wrum());
    }
}
