package common.util;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.*;

public class SerializeUtils {

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream objOS = new ObjectOutputStream(os);

        try {
            objOS.writeObject(obj);
            objOS.flush();
            return os.toByteArray();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            os.close();
            objOS.close();
        }

        return null;
    }

    public static Object deSerialize(byte[] buf) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(buf);
        ObjectInputStream objIS = new ObjectInputStream(is);
        try {
            return objIS.readObject();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            is.close();
            objIS.close();
        }

        return null;
    }

}
