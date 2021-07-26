package secnj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.util.HashMap;


public class  RuleEngine{
    private final static HashMap<String, JSONArray> rules = new HashMap<>();
    public static void main(String[] args){
      RuleEngine test =  new RuleEngine();

      test.RuleCheck(new JSONObject ());
    }

    public RuleEngine(){
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


    public JSONObject RuleCheck(JSONObject res){

        for(Object j: rules.get("ssh")){

            System.out.println(j);
        }

        return new JSONObject();

    }
}