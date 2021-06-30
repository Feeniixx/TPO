/**
 *
 *  @author Golunko Lizaveta S19240
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientTask implements Runnable
{

    private final Client c;
    private final List<String> reqList;
    private final boolean showSendRes;
    boolean isReady = false;
    String clog;

    public ClientTask(Client c, List<String> reqList, boolean showSendRes)
    {
        this.c = c;
        this.reqList = reqList;
        this.showSendRes = showSendRes;
    }

    public static ClientTask create(Client c, List<String> reqList, boolean showSendRes)
    {
        return new ClientTask(c, reqList, showSendRes);
    }

    public String get() throws InterruptedException, ExecutionException
    {
        while(!isReady)
            Thread.sleep(200);
        return clog;
    }

    @Override
    public void run()
    {
        c.connect();
        c.send("login " + c.id);
        for(String req : reqList)
        {
            String res = c.send(req);
            if(showSendRes)
                System.out.println(res);
        }
        clog = c.send("bye and log transfer");
        isReady = true;
    }
}
