package common.service;

import common.pojo.Person;

public interface HelloService {
    String sayHello(String name);
    Person getPerson(String name);
}