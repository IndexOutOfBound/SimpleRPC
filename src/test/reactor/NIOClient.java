import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException, InterruptedException{
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(12345);
        socketChannel.connect(inetSocketAddress);

        RandomAccessFile file = new RandomAccessFile(
                NIOClient.class.getClassLoader().getResource("test.txt").getFile()
                , "rw");

        FileChannel fileChannel = file.getChannel();
        fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        fileChannel.close();
        file.close();
        socketChannel.close();
    }
}
