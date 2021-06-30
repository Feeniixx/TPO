package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatClient {

    private final InetSocketAddress inetSocketAddress;
    private final StringBuilder clientView;
    private final String clientId;
    private SocketChannel channel;
    private final Thread thread = new Thread(this::run);
    private SocketChannel channel1;

    private final Lock lock = new ReentrantLock();

    public ChatClient(String host, int port, String id) {
        this.inetSocketAddress = new InetSocketAddress(host, port);
        this.clientId = id;

        clientView = new StringBuilder("=== " + clientId + " chat view\n");
    }

    public void login() {

        try {
            channel = SocketChannel.open(inetSocketAddress);
            channel.configureBlocking(false);

            send("log in " + clientId);

            thread.start();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void logout() {

        send("log out" + "#");

        try {
            lock.lock();
            thread.interrupt();

        } catch (Exception exception) {
            exception.printStackTrace();

        } finally {
            lock.unlock();
        }
    }



    public String getChatView() {

        return clientView.toString();
    }

    public void send(String req) {

        try {
            Thread.sleep(20);
            channel.write(StandardCharsets.UTF_8.encode(req + "#"));
            Thread.sleep(20);

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void run() {

        int capacity = 1024;
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
        int bytesRead = 0;

        while (true) {
            if (thread.isInterrupted()) break;
            do {
                try {
                    lock.lock();
                    bytesRead = channel.read(buffer);

                } catch (Exception exception) {
                    exception.printStackTrace();

                } finally {
                    lock.unlock();
                }
            } while (bytesRead == 0 && !thread.isInterrupted());

            buffer.flip();
            String response = StandardCharsets.UTF_8.decode(buffer).toString();
            clientView.append(response);
            buffer.clear();
        }
    }

    private void setUpServer(String host, int port) throws IOException {
        channel1 = SocketChannel.open();
        channel1.configureBlocking(false);
        channel1.connect(new InetSocketAddress(host, port));

        while (true) {
            if (channel1.finishConnect()) break;
        }

    }
}
