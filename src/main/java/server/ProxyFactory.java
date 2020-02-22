package server;

import client.RPCClient;
import common.util.NetModel;
import common.util.SerializeUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static InvocationHandler invocationHandler = new InvocationHandler() {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            NetModel netModel = new NetModel();
            Class<?>[] classes = proxy.getClass().getInterfaces();
            String className = classes[0].getName();

            netModel.setClassName(className);
            netModel.setMethod(method.getName());
            netModel.setArgs(args);

            String[] types = null;

            if(args != null){
                types = new String[args.length];
                for (int i =0; i < types.length; i++){
                    types[i] = args[i].getClass().getName();
                }
            }

            netModel.setTypes(types);

            byte[] bytes = SerializeUtils.serialize(netModel);

            return RPCClient.send(bytes);
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz){
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }
}
