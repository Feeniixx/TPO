/**
 *
 *  @author Golunko Lizaveta S19240
 *
 */

package zad1;

import org.yaml.snakeyaml.Yaml;


import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;


public class Tools {
    static Options createOptionsFromYaml(String fileName) throws Exception{
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String doc="";
            String line;
            while((line=bufferedReader.readLine())!=null)
                doc+=line+'\n';
            Yaml yaml = new Yaml();
            Map map = (Map) yaml.load(doc);
            bufferedReader.close();
            return new Options(map.get("host").toString(),(int)map.get("port"),(boolean)map.get("concurMode"),(boolean)map.get("showSendRes"),(Map)map.get("clientsMap"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
