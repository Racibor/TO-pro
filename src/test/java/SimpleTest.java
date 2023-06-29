import mock.InvocationDetails;
import mock.Mock;
import mock.MockContainer;
import mock.arguments.captor.ArgumentCaptor;
import mock.arguments.matcher.Matcher;
import mock.verification.InvocationAtLeastFilteredVerificationStrategy;
import mock.verification.InvokationTimesFilteredVerificationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

public class SimpleTest {

    @Test
    public void thenReturn() {
        Car car = Mock.mock(Car.class);
        Assertions.assertEquals(0, car.getGear());

        Mock.when(car.getGear()).thenReturn(1);
        Assertions.assertEquals(1, car.getGear());
    }

    @Test
    public void thenAnswer() {
        Car car = Mock.mock(Car.class);

        int offset = 2;
        int gearToSet = 2;

        Mock.doAnswer(metadata -> {
            Object[] arguments = metadata.getArguments();
            Integer gear = (Integer) arguments[0];
            ((Car) metadata.getMock()).gear = gearToSet + offset;
            return Void.TYPE;
        }).when(car).setGear(Matcher.anyInt());

        car.setGear(gearToSet);

        Assertions.assertEquals(gearToSet + offset, car.getGear());
    }

    @Test
    public void thenThrow() {
        Car car = Mock.mock(Car.class);
        Mock.when(car.getGear()).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> {
            car.getGear();
        });
    }

    @Test
    public void thenAnswerWithArgumentMatching() {
        Car car = Mock.mock(Car.class);
        Mock.doAnswer(metadata -> {
            return Void.TYPE;
        }).when(car).setGear(Matcher.eq(2));

        car.setGear(1);
        Assertions.assertEquals(1, car.getGear());
        car.setGear(2);
        Assertions.assertNotEquals(2, car.getGear());
    }

    @Test
    public void chainedMockTest() {
        Car car = Mock.mock(Car.class);
        Mock.doReturn(1).when(car).getGear();
        Mock.doReturn(2).when(car).getGear();
        Mock.doThrow(RuntimeException.class).when(car).getGear();
        Mock.doReturn(3).when(car).getGear();

        Assertions.assertEquals(1, car.getGear());
        Assertions.assertEquals(2, car.getGear());
        Assertions.assertThrows(RuntimeException.class, () -> {
            car.getGear();
        });
        Assertions.assertEquals(3, car.getGear());
        Assertions.assertEquals(3, car.getGear());
    }

    @Test
    public void verifyCertaiInvoked() {
        Car car = Mock.mock(Car.class);
        car.setGear(3);

        Mock.verify(car).setGear(Matcher.anyInt());
    }

    @Test
    public void verifyCertainArgumentsInvokedCertainAmountOfTimes() {
        Car car = Mock.mock(Car.class);
        car.setGear(3);
        car.setGear(3);

        Mock.verify(car, new InvokationTimesFilteredVerificationStrategy(2)).setGear(Matcher.eq(3));
    }

    @Test
    public void verifyCertainArgumentsInvokedAtLeastAmountOfTimes() {
        Car car = Mock.mock(Car.class);
        car.setGear(3);
        car.setGear(3);
        car.setGear(3);

        Mock.verify(car, new InvocationAtLeastFilteredVerificationStrategy(2)).setGear(Matcher.eq(3));
    }

    @Test
    public void verifyCertainArgumentsInvokedAtLeastAmountOfTimesWithArgumentMatching() {
        Car car = Mock.mock(Car.class);
        car.setGear(3);
        car.setGear(4);
        car.setGear(3);
        car.setGear(4);
        car.setGear(5);
        car.setGear(3);

        Mock.verify(car, new InvocationAtLeastFilteredVerificationStrategy(2)).setGear(Matcher.eq(3));
        Mock.verify(car, new InvokationTimesFilteredVerificationStrategy(2)).setGear(Matcher.eq(4));
    }

    @Test
    public void captorTestOnSpy() {
        Car car = new Car();
        Car spy = Mock.spy(car);

        ArgumentCaptor<String> captor = new ArgumentCaptor();

        ConcurrentHashMap m = MockContainer.mockContainer;
        spy.placeLuggage("test", 2);
        spy.placeLuggage("test2", 3);
        spy.placeLuggage("test3", 3);
        Mock.verify(spy, new InvocationAtLeastFilteredVerificationStrategy(1)).placeLuggage(captor.capture(), 3);

        List<String> itemsThatWerePlacedThreeTimesAtOnce = captor.getValues();
        Assertions.assertEquals(List.of("test2", "test3"), itemsThatWerePlacedThreeTimesAtOnce);
    }


    @Test
    public void multithreadingTest1() throws InterruptedException {
        Car car = Mock.mock(Car.class);

        Object lock = new Object();


        Runnable task1 = () -> {
            try {
                synchronized (lock) {
                    InvocationDetails<String> temp = Mock.when(car.threadTesting("thread1"));
                    lock.notify();
                    lock.wait();
                    temp.thenReturn("thread1");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable task2 = () -> {
            try {
                synchronized (lock) {
                    lock.wait();
                    Mock.when(car.threadTesting("thread2")).thenReturn("thread2");
                    lock.notify();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread t1 =  new Thread(task2);
        Thread t2 =  new Thread(task1);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Assertions.assertEquals("thread1", car.threadTesting("thread1"));
        Assertions.assertEquals("thread2", car.threadTesting("thread2"));
    }




}
