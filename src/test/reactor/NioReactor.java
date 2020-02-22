
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NioReactor {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 初始化reactor
     */
    public NioReactor(int port){
        try {
            //初始化实际工作的线程池
            threadPoolExecutor = new ThreadPoolExecutor(5,10,
                    100l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            //创建一个selector
            selector = Selector.open();
            //创建一个server socket channel
            serverSocketChannel = ServerSocketChannel.open();
            //设定socket channel为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //绑定socket channel与selector
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //初始化acceptor，并将其绑定到key上
            selectionKey.attach(new Acceptor());
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Acceptor implements Runnable{

        public void run() {
            try{
                SocketChannel  socketChannel = serverSocketChannel.accept();
                if( socketChannel != null ){
                    new Handler(socketChannel, selector);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchLoop() throws IOException{
        while (true){
            selector.select();
            selector.selectedKeys().forEach(
                    this::dispatchTask
            );
            selector.selectedKeys().clear();
        }
    }

    private void dispatchTask(SelectionKey selectionKey){
        Runnable runnable = (Runnable) selectionKey.attachment();
        if( runnable != null ){
            threadPoolExecutor.execute(runnable);
        }
    }

    public static void main(String[] args){
        NioReactor nioReactor;
        try {
            nioReactor = new NioReactor(12345);
            nioReactor.dispatchLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
