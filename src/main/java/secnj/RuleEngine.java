package secnj;

import org.json.JSONArray;
import java.io.File;
import java.util.HashMap;

public class  RuleEngine{
    public static void main(String[] args){
        new RuleEngine();
    }

    public RuleEngine(){
        HashMap<String, JSONArray> rules = new HashMap<>();
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
}