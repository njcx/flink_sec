package secnj;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.PropertiesUtil;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class utils {


    public static void main(String[] args){

        System.out.println(utils.GetJsonRule("rules/dns.json").toString());
    }

    public static ParameterTool getParameterTool(){

        final Logger log = LoggerFactory.getLogger(utils.class);

        try {
            return ParameterTool
                    .fromPropertiesFile(PropertiesUtil.class.getResourceAsStream("application.properties"));
        }catch (IOException e){
            log.error("读取配置文件错误",e);
        }
        return ParameterTool.fromSystemProperties();
    }


    public static JSONArray GetJsonRule (String name) {

        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray(stringBuffer.toString());
    }
}

