# **About**

Project is an attempt to create a mocking library with features similar to popular mocking frameworks/libraries (ie. Mockito, PowerMock).

The library as of now is capable of:
- creating Mocks out of any Class or interface
- overriding an object's method behaviour by forcing it to return a certain value or executing specified expression
- matching a result or expression to a given argument boundaries with the use of argument matchers
- simulating exception being thrown from a method

# **Mocking process**

The base idea behind mocking libraries is to create a fake object, which behaviour a programmer can easily control. This library uses ByteBuddy to dynamicly create a subclass of class that is desired to be mocked and an object is instantiated with the help of objenesis library.

example of making a Car class mock

`Car car = Mocker.mock(Car.class);`

After the object creation the methods' default behaviours are overriden by a mocking interceptor. Mocking interceptor's task is to keep track of the mocked object's methods invocations and it's metadata (call arguments, class name, method name).
If it's the first call with the same metadata it is assumed that it is a part of mocking process, which means that such data is stored for later use. If the same metadata is being used more times, the associated fake return expression is executed.
The first invocation is best fit in the `Mocker.when()` method, as it tracks down an invocation object and returns it for further usage.

# **Method overriding**

There are three ways a method can be overwritten. First way is a single result that is instantly returned from the mocked object. Second way gives access to call metadata (call arguments, class name, method name) and an expression can be built that way. Third way forces a method to throw an exception.
Internally all of these are treated as expressions as they use a single interface `MockedExpression` but implement it differently.

