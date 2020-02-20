package common.dao;

import common.pojo.Person;
import common.util.Gender;

import java.util.HashMap;

public class PersonRepository {

    private HashMap<String, Person> persons;

    public PersonRepository(){
        persons = new HashMap<String, Person>();
        Person p1 = Person.builder().name("weikai").gender(Gender.MALE).age(26).build();
        Person p2 = Person.builder().name("linyan").gender(Gender.FEMALE).age(27).build();
        persons.put(p1.getName(), p1);
        persons.put(p2.getName(), p2);
    }

    public Person get(String name){
        return persons.get(name);
    }
}
