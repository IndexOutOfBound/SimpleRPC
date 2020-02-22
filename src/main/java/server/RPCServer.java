package server;

import common.util.NetModel;
import common.util.SerializeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RPCServer {
    public static void main(String[] args)
    {
        try {
            openServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //simple server
    public static void openServer(){

        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true){
                System.out.println("Server started");
                Socket socket = serverSocket.accept();
                System.out.println("Get new request");
                InputStream in = socket.getInputStream();
                byte[] buff = new byte[1024];
                in.read(buff);
                byte[] formatData = formatData(buff);
                OutputStream out = socket.getOutputStream();
                out.write(formatData);
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //handle the request
    // deserialize the byte data to get object name, method name, args
    // calculate the response and serialize it into a byte array
    // return it
    public static byte[] formatData(byte[] data){
        try {
            //先反序列化byte数组成NetModel
            NetModel netModel = (NetModel) SerializeUtils.deSerialize(data);
            String className = netModel.getClassName();
            String[] types = netModel.getTypes();
            Object[] args = netModel.getArgs();


            Map<String, String> map = new HashMap<String,String>();
            map.put("common.service.HelloService", "common.service.HelloServiceImpl");
            Class<?> clazz = Class.forName(map.get(className));
            Class<?>[] typeClazzs = null;
            if(types != null){
                typeClazzs = new Class[types.length];
                for (int i = 0; i < typeClazzs.length; i++){
                    typeClazzs[i] = Class.forName(types[i]);
                }
            }

            Method method = clazz.getMethod(netModel.getMethod(), typeClazzs);
            Object object = method.invoke(clazz.newInstance(), args);
            byte[] byteArray = SerializeUtils.serialize(object);
            return byteArray;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}
