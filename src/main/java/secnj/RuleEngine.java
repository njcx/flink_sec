package secnj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class  RuleEngine{

    private static HashMap<String, JSONArray> rules;
    private static  Config config ;

    static {
         rules = new HashMap<>();
         config =  ConfigFactory.load();
        String path = Thread.currentThread().getContextClassLoader().getResource("rules").getPath();
        File file = new File(path);
        File[] fs = file.listFiles();
        for(File f:fs){
            if(!f.isDirectory()){
                String ruleType =  f.getName().substring(0,f.getName().lastIndexOf("."));
                rules.put(ruleType,utils.GetJsonRule("rules/"+f.getName()));
            }
        }
    }

    public static JSONObject RuleCheck(Map<String, Object> res){

        String type = res.get(config.getString("kafka.type.key")).toString();

        if(type.contains("conn")){
            for(Object j: rules.get("ssh")){
                System.out.println(j);
            }
        }

        if(type.contains("dns")){

            for(Object j: rules.get("dns")){
                System.out.println(j);
            }

        }



        if(type.contains("http")){

            for(Object j: rules.get("http")){
                System.out.println(j);
            }


        }



        if(type.contains("mysql")){

            for(Object j: rules.get("mysql")){
                System.out.println(j);
            }


        }

        if(type.contains("ssh")){
            for(Object j: rules.get("ssh")){
                System.out.println(j);
            }
        }

        return new JSONObject();

    }
}