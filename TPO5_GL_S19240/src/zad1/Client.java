/**
 *
 *  @author Golunko Lizaveta S19240
 *
 */

package zad1;





import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client
{

    SocketChannel channel;
    String host;
    int port;
    String id;


    private static Charset charset = Charset.forName("ISO-8859-2");
    private static final int BSIZE = 1024;


    private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);


    private StringBuffer reqString = new StringBuffer();

    public Client(String host, int port, String id)
    {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void connect()
    {
        try
        {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));

            while(!channel.finishConnect())
            {

            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String send(String req)
    {

        CharBuffer cbuf = CharBuffer.wrap(req);
        ByteBuffer outBuf = charset.encode(cbuf);

        try
        {
            outBuf.clear();
            outBuf.put(req.getBytes());
            outBuf.flip();

            try
            {
                channel.write(outBuf);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }


            while(true)
            {
                bbuf.clear();
                int readBytes = channel.read(bbuf);
                if(readBytes == 0)
                {
                    continue;
                }
                else if(readBytes == -1)
                {
                    break;
                }
                else
                {
                    bbuf.flip();
                    String response = new String(bbuf.array(), 0, readBytes);
                    return response;
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return " ";
    }
}
