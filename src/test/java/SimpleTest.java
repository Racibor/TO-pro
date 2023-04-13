import mock.Mock;
import mock.arguments.matcher.Matcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SimpleTest {

    @Test
    public void answerTest() {
        Car car = Mock.mock(Car.class);
        Integer arg11 = 5;
        Integer arg22 = 5;
        Mock.when(car.wrum(Matcher.any(), Matcher.eq("test"), Matcher.any())).thenAnswer(metadata -> {
            Object[] arguments = metadata.getArguments();
            Integer arg1 = (Integer) arguments[0];
            Integer arg3 = (Integer) arguments[2];
            System.out.println("mocked message: " + ((String)arguments[1]));
            return (long) (arg1 + arg3);
        });
        Assertions.assertEquals(arg11 + arg22, car.wrum(arg11, "test", arg22));
        Assertions.assertNotEquals(arg11 + arg22, car.wrum(arg11, "nonTest", arg22));
    }

    @Test
    public void throwableTest() {
        Car temp = Mockito.mock(Car.class);
        Mockito.when(temp.wrum(4, "test", 5)).thenReturn(5L).thenThrow(RuntimeException.class);
        Car car = Mock.mock(Car.class);
        Integer arg11 = 5;
        Integer arg22 = 5;
        Mock.when(car.wrum(Matcher.any(), Matcher.eq("throw"), Matcher.any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> {
           car.wrum(arg11, "throw", arg22);
        });
    }


}
