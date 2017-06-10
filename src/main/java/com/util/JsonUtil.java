package com.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;
/**
 * json解析工具
 * @author gzw
 *
 */
public class JsonUtil
{
	public static List<Map<String, Object>> parseJSON2List(String jsonStr) throws Exception{
        JSONArray jsonArr = new JSONArray(jsonStr);
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<jsonArr.length();i++){
            JSONObject json2 = jsonArr.getJSONObject(i);
            list.add(parseJSON2Map(json2.toString()));
        }
        return list;
    }
    
   
    public static Map<String, Object> parseJSON2Map(String jsonStr) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        JSONObject json =new JSONObject(jsonStr);
        Iterator<?> itr = json.keys();
        while(itr.hasNext()){
            String key = (String) itr.next(); 
            String value=json.get(key).toString();
            if(value==null){
            	map.put(key, value);
            }else if(value.startsWith("[")&&value.endsWith("]")){  
                map.put(key, parseJSON2List(value));  
            }else if(value.startsWith("{")&&value.endsWith("}")){  
                map.put(key, parseJSON2Map(value));  
            }else{  
                map.put(key, value);  
            } 
        }
        return map;
    }
    public static boolean isLeapYear(int year){
        boolean isLeapYear=false;
        if((year%4==0&&year%100==0)||year%400==0){
            isLeapYear=true;
        }
        return  isLeapYear;
    }
    public static  int leapYearfeDay(int month,int year){
        int februaryDay=29;
        boolean flag=isLeapYear(year);
        if(!flag){
            februaryDay=28;
        }
        return februaryDay;
    }
    public static  int maxDay(int month){
        int maxDay=30;
        if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
            maxDay=31;
        }
        return  maxDay;
    }
}
