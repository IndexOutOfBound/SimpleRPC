package common.service;

import common.dao.PersonRepository;
import common.pojo.Person;

public class HelloServiceImpl implements HelloService {

    PersonRepository personRepository = new PersonRepository();

    public String sayHello(String name) {

        return "Hello, " + name;
    }

    public Person getPerson(String name) {
        return personRepository.get(name);
    }
}
