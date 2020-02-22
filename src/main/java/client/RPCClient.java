package client;

import common.service.HelloService;
import common.util.SerializeUtils;
import server.ProxyFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RPCClient {

    public static Object send(byte[] bs){
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(bs);
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            in.read(buf);
            Object formatData = SerializeUtils.deSerialize(buf);
            socket.close();
            return formatData;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args){
        HelloService helloService = ProxyFactory.getInstance(HelloService.class);
        System.out.println("say:"+helloService.sayHello("weikai"));
        System.out.println("Person:"+helloService.getPerson("weikai"));
    }
}
