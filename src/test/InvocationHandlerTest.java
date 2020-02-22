
import common.service.HelloService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InvocationHandlerTest {



    public static void main(String[] args){
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                System.out.println("invocation handler");
                Class<?> clazz = Class.forName("ProxyTest");
                method.invoke(clazz.newInstance(), args);

                return "invoke";
            }
        };

        HelloService test = new ProxyTest();

        HelloService service = (HelloService)Proxy
                .newProxyInstance(
                        test.getClass().getClassLoader(),
                        test.getClass().getInterfaces(),
                        handler);

        service.sayHello("weikai");

    }

}
