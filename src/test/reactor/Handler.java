
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Handler implements Runnable{

    private SocketChannel socketChannel;
    private SelectionKey selectionKey;
    private ByteBuffer olbBuffer;


    public Handler(SocketChannel socketChannel, Selector selector){
        olbBuffer = null;
        try {
            //设置socket的阻塞模式
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            //将socket channel与selector绑定并得到selection key
            selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

            //将此对象与selection key绑定
            selectionKey.attach(this);

            //唤醒阻塞的selector，使其中的select方法立刻返回
            selector.wakeup();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            readData(selectionKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据，进行操作，并最终返回
     * @param selectionKey
     */
    private void readData(SelectionKey selectionKey) throws IOException {
        ByteBuffer newBuffer = ByteBuffer.allocate(64);

        int read;
        if(( read = socketChannel.read(newBuffer) ) <= 0 ){
            return;
        }

        newBuffer.flip();
        String line = readLine(newBuffer);

        if( line != null){

            String sendData = readLine(mergeBuffer(olbBuffer, newBuffer));
            if( readLineContent(sendData).equalsIgnoreCase("exit") ){
                socketChannel.close();
                return;
            }

            ByteBuffer sendBuffer = ByteBuffer.wrap(sendData.getBytes("utf-8"));
            while (sendBuffer.hasRemaining()){
                socketChannel.write(sendBuffer);
            }
            olbBuffer = null;
        } else {
            olbBuffer = mergeBuffer(olbBuffer, newBuffer);
        }


    }


    private String readLine(ByteBuffer byteBuffer) throws UnsupportedEncodingException {

        char CR = '\r';
        char LF = '\n';

        boolean crFound = false;

        int index = 0;
        int len = byteBuffer.limit();
        byteBuffer.rewind();
        while (index < len){
            Byte temp = byteBuffer.get();
            if(temp == CR){
                crFound = true;
            }

            if(crFound && temp == LF){
                return new String(Arrays.copyOf(byteBuffer.array(), index + 1), "utf-8");
            }
            index++;
        }

        return null;
    }

    private String readLineContent(String line) throws UnsupportedEncodingException{
        System.out.println(line);
        System.out.println(line.length());
        return line.substring(0, line.length() - 2);
    }

    private ByteBuffer mergeBuffer(ByteBuffer oldBuffer, ByteBuffer newBuffer){

        if( oldBuffer == null ){
            return newBuffer;
        }

        newBuffer.rewind();
        if( oldBuffer.remaining() > ( newBuffer.remaining() - newBuffer.position())){
            return oldBuffer.put(newBuffer);
        }

        //如果不是以上这两种情况就构建新的buffer进行拼接
        int oldSize = oldBuffer.limit();
        int newSize = newBuffer.limit();

        ByteBuffer buffer = ByteBuffer.allocate(oldSize + newSize);
        buffer.put(Arrays.copyOfRange(oldBuffer.array(), 0, oldSize));
        buffer.put(Arrays.copyOfRange(newBuffer.array(), 0, newSize));

        return buffer;
    }

}
