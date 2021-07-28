package secnj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


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

    public static String ResCheck(Map<String, Object> res){

        String type = res.get(config.getString("kafka.type.key")).toString();
        if(type.contains("conn")){
            for(Object j: rules.get("ssh")){
                if (RuleEngine.RuleCheck(res,j)){
                    res.put("rule",j);
                    return JsonUnflattener.unflatten(res);
                }
            }
        }

        if(type.contains("dns")){
            for(Object j: rules.get("dns")){
                if (RuleEngine.RuleCheck(res,j)){
                    res.put("rule",j);
                    return JsonUnflattener.unflatten(res);
                }
            }
        }

        if(type.contains("http")){
            for(Object j: rules.get("http")){
                if (RuleEngine.RuleCheck(res,j)){
                    res.put("rule",j);
                    return JsonUnflattener.unflatten(res);
                }
            }
        }
        if(type.contains("mysql")){
            for(Object j: rules.get("mysql")){
                if (RuleEngine.RuleCheck(res,j)){
                    res.put("rule",j);
                    return JsonUnflattener.unflatten(res);
                }
            }
        }
        if(type.contains("ssh")){
            for(Object j: rules.get("ssh")){
                if (RuleEngine.RuleCheck(res,j)){
                    res.put("rule",j);
                    return JsonUnflattener.unflatten(res);
                }
            }
        }
        return "{}";

    }

    public static HashMap<String, String> objectToMap(Object obj) {
        if (obj ==null) {
            return null;
        }
        HashMap<String, String> map =new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") ==0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter !=null ? getter.invoke(obj) :null;
                map.put(key, value.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static boolean RuleCheck(Map<String, Object> res, Object rule_) {
        JSONObject rule = JSONObject.parseObject(rule_.toString());
        if (rule.get("state").toString().equals("enable")) {
            int rulehit = 0;
            JSONArray detect_list = JSON.parseArray(rule.get("detect_list").toString());



            for (int i = 0; i < detect_list.size(); i++) {
                HashMap<String, String> detect_item = JSONObject.parseObject(JSONObject.toJSONString(detect_list.get(i)),HashMap.class);
                try {
                        if (detect_item.get("type").equals("re")) {

                            if (Pattern.matches(detect_item.get("rule"), res.get(detect_item.get("field")).toString())) {
                                rulehit++;
                            }
                        }

                        if (detect_item.get("type").equals("in")) {
                            if (res.get(detect_item.get("field")).toString().contains(detect_item.get("rule"))) {
                                rulehit++;
                            }
                        }

                        if (detect_item.get("type").equals("equal")) {
                            if (res.get(detect_item.get("field")).toString().equals(detect_item.get("rule"))) {
                                rulehit++;
                            }
                        }
                }
                catch  (Exception e) {
                }
            }

            if (rule.get("rule_type").toString().equals("and")) {
                if (rulehit == detect_list.size()) {
                        return true;
                }
                if (rule.get("rule_type").toString().equals("or")) {
                    if (rulehit > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}