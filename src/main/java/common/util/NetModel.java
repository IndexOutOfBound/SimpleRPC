package common.util;


import lombok.Data;

import java.io.Serializable;

@Data
public class NetModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private String className; //接口名

    private String method; //方法名

    private  Object[] args ; //参数

    private String[] types; //参数类型

}
