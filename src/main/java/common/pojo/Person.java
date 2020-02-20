package common.pojo;

import common.util.Gender;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Person implements Serializable{

    private static final long serialVersionUID = 1L;

    private String name;

    private Gender gender;

    private Integer age;
}
