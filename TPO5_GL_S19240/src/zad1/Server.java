

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server implements Runnable
{
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    SelectionKey selectionKey;

    String requestFromClient;
    StringBuilder sb = new StringBuilder();
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    LocalTime localTime = LocalTime.now(zone);

    HashMap<SocketChannel, String> stringHashMap = new HashMap<>();

    boolean serverIsRunning;

    Map<SocketChannel, String> map = new HashMap<>();

    String host;
    int port;

    public Server(String host, int port)
    {
        this.host = host;
        this.port = port;

    }

    public void startServer()
    {
        new Thread(this).start();
        serverIsRunning = true;
    }

    public void stopServer()
    {
        serverIsRunning = false;
    }

    public String getServerLog()
    {
        return sb.toString();
    }

    @Override
    public void run()
    {
        try
        {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        while(serverIsRunning)
        {
            try
            {
                selector.selectNow();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();

                while(iter.hasNext())
                {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if(key.isAcceptable())
                    {
                        SocketChannel clientSocket = serverSocketChannel.accept();
                        clientSocket.configureBlocking(false);
                        clientSocket.register(selector, SelectionKey.OP_READ);
                        continue;
                    }
                    if(key.isReadable())
                    {

                        SocketChannel clientSocket = (SocketChannel)key.channel();

                        serviceRequest(clientSocket);
                        continue;
                    }

                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                continue;
            }
        }
    }


    public void serviceRequest(SocketChannel channel)
    {
        Time time = new Time();
        ByteBuffer bbuf = ByteBuffer.allocate(1024);

        if(!channel.isOpen())
            return;

        int readBytes = 0;

        try
        {
            readBytes =  channel.read(bbuf);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        requestFromClient = new String(bbuf.array(), 0 , readBytes);

        Pattern liczbyPattern = Pattern.compile("[0-9]");
        Matcher matcher = liczbyPattern.matcher(requestFromClient);

        if(requestFromClient.contains("login"))
        {
            String elements[] = requestFromClient.split(" ");
            stringHashMap.put(channel, elements[1]);
            sb.append(stringHashMap.get(channel) + " logged in at " + localTime + "\n");

            map.put(channel, map.get(channel) + "=== " + stringHashMap.get(channel) + " log start === " + "\nlogged in");
            try
            {
                channel.write(ByteBuffer.wrap("logged in".getBytes()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(matcher.find())
        {

            sb.append(stringHashMap.get(channel) + " request at " + localTime + ": " + requestFromClient +  "\n");

            String data;
            if(!requestFromClient.contains("T"))
            {
                data = new String(requestFromClient.getBytes(), 0, 21);
            }
            else
            {
                data = new String(requestFromClient.getBytes(), 0, 33);
            }
            String elements[] = data.split(" ");

            try
            {
                String dataOdDataDo = time.passed(elements);
                channel.write(ByteBuffer.wrap(dataOdDataDo.getBytes()));
                map.put(channel, map.get(channel) + "Request:  " + requestFromClient + " \nResult: \n" + dataOdDataDo);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(requestFromClient.equals("bye"))
        {
            try
            {
                channel.write(ByteBuffer.wrap("logged out".getBytes()));
                map.put(channel, map.get(channel) + "\nlogged out ");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(requestFromClient.contains("bye and log transfer"))
        {
            sb.append(stringHashMap.get(channel) + " logged out at " + localTime + "\n");

            map.put(channel, map.get(channel) + "\nlogged out\n=== " +  stringHashMap.get(channel) + " log end ===\n");
            map.put(channel, map.get(channel) + "\nlogged out ");

            try
            {

                channel.write(ByteBuffer.wrap(map.get(channel).getBytes()));
                channel.close();
                channel.socket().close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
