
import common.pojo.Person;
import common.service.HelloService;

public class ProxyTest implements HelloService {
    public String sayHello(String name) {
        System.out.println("proxyTest");
        return null;
    }

    public Person getPerson(String name) {
        return null;
    }
}
