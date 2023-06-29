# **About**

Project is an attempt to create a mocking library with features similar to popular mocking frameworks/libraries (ie. Mockito, PowerMock).

The library as of now is capable of:
- creating Mocks out of any Class or interface
- overriding an object's method behaviour by forcing it to return a certain value or executing specified expression
- chaining return calls, so we can reproduce a sequence of return values
- matching a result or expression to a given argument boundaries with the use of argument matchers
- simulating exception being thrown from a method
- method verification checking if a method with given arguments had been called, how many times had it been called and capturing previous calls arguments.
- spying on an already instantiated object (overriding it's methods, verification)
- library supports simple multithreading with each of it's mocking process happening locally in a thread


Below there are some examples you can follow. More detailed clarification in a HowItWorks.MD file.
# **Simple Mocking**

example of making a Car class mock:

`Car car = Mock.mock(Car.class);`

example of creating a spy:

```
Car car = new Car();
Car spy = Mock.spy(car);
```

this objects are ready to work with. 

# **Method overriding**

There are three ways a method can be overwritten. First way is a single result that is instantly returned from the mocked object. 
`Mock.when(car.drive()).thenReturn(5)`

if the method you are overriding returns nothing, then you have to use a different approach:

`doReturn(5).when(car).drive();
`
Second way gives access to call metadata (call arguments, class name, method name) and an expression can be built that way. 

```
Mock.when(car.drive()).thenAnswer(metadata -> {
    do something...
});

doAnswer(matadata -> {
    do something...
}).when(car).drive();

```

Third way forces a method to throw an exception.

```
Mock.when(car.drive()).thenThrow(NullPointerException.class);

doThrow(NullPointerException.class).when(car).drive();

```

# **Arguments matching**

You can use a system of matchers with mocks to create dedicated handling based on arguments.

```
Mock.when(car.drive(Matcher.any(), Matcher.eq("5")));
```

Matcher any() allows all kinds of argumnts to pass, whereas eq checks if te given argument is equal to a provided objct.

```
Mock.when(car.drive(Matcher.any(), Matcher.eq("5"))).thenReturn(2);
Mock.when(car.drive(Matcher.any(), Matcher.eq("6"))).thenReturn(3);

car.drive(4, "5") -> returns 2
car.drive(4, "6") -> returns 3
```
You can find more Matchers in the Matcher class definition

# **Method verification**

Tha available methods of verification are
- checking if given method with certain arguments was called, and specified times
- collect previously passed arguments

verify if method had been called:

```
Mock.doReturn(Boolean.FALSE).when(spy).add(Matcher.eq("test"));
spy.add("test");
Mock.verify(spy).add("test"); -> will pass
Mock.verify(spy).add("test2"); -> will fail
```

verify if it was called desired ammoun of times:

```
Mock.doReturn(Boolean.FALSE).when(spy).add(Matcher.eq("test"));
spy.add("test");
spy.add("test");
Mock.verify(spy, new InvocationAtLeastFilteredVerificationStrategy(2)).add("test"); -> will pass
Mock.verify(spy, new InvocationAtLeastFilteredVerificationStrategy(3)).add("test"); -> will fail

```

collect previously passed arguments with ArgumentCaptor

```
ArgumentCaptor<Integer> captor = new ArgumentCaptor();

car.drive(6, "", 24);
car.drive(7, "", 24);
Mock.verify(car).drive(captor.capture(), Matcher.any(), Matcher.any());


Integer last = captor.getLastValue(); -> will hold value 7
```




