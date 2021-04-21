
package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {
    public Request() {
    }

    public Response connection(String url) {
        try {


           HttpURLConnection con = (HttpURLConnection)(new URL(url)).openConnection();
            int responses = con.getResponseCode();
            InputStream inputstream;
            if (responses >= 200 && responses <= 299) {
                inputstream = con.getInputStream();
            } else {
                inputstream = con.getErrorStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
            StringBuilder response = new StringBuilder();

            String currentLine;
            while((currentLine = br.readLine()) != null) {
                response.append(currentLine);
            }

            br.close();
            con.disconnect();
            return new Response(responses, response.toString());
        } catch (IOException var8) {
            var8.printStackTrace();
            return null;
        }
    }
}
