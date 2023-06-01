import mock.Mock;
import mock.arguments.matcher.Matcher;
import mock.verification.InvocationAtLeastFilteredVerificationStrategy;
import mock.verification.InvokationAtLeastVerificationStrateg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

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
        Mock.verify(car, new InvocationAtLeastFilteredVerificationStrategy(2)).wrum(arg11, Matcher.any(), arg22);
    }

    @Test
    public void throwableTest() {
        ArrayList<String> l = new ArrayList<>();
        ArrayList<String> spy = Mock.spy(l);

        /*TestContainer<String> temp = new TestContainer<>(new ArrayList<>());
        TestContainer<String> spy = Mock.spy(temp);*/
        Mock.when(spy.add("test")).thenReturn(false);
        spy.add("test");
        spy.add("test");
        spy.forEach(System.out::println);

//        Car car = Mock.mock(Car.class);
//        Integer arg11 = 5;
//        Integer arg22 = 5;
//        Mock.when(car.wrum(Matcher.any(), Matcher.eq("throw"), Matcher.any())).thenThrow(NullPointerException.class);
//        Assertions.assertThrows(NullPointerException.class, () -> {
//            System.out.println("before the cataclysm number 1");
//            car.wrum(arg11, "throw", arg22);
//            System.out.println("we survived!!! number 1");
//        });
//        Assertions.assertDoesNotThrow(() -> {
//            System.out.println("before the cataclysm number 2");
//            car.wrum(arg11, "notThrow", arg22);
//            System.out.println("we survived!!! number 2");
//        });

//        Car temp = Mockito.mock(Car.class);
//        Mockito.when(temp.wrum(2, "test", 4));
//        temp.wrum(2,"test", 4);
//        Mockito.verify(temp, Mockito.times(2)).wrum(2,"test", 4);
    }


}
