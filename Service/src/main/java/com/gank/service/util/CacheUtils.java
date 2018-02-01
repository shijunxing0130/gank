package com.gank.service.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;



@SuppressWarnings("all")
public class CacheUtils {


    private static final Logger logger = LoggerFactory.getLogger(CacheUtils.class);


    public static final <T> List<T> getList(String jsontext, String list_str,
                                            Class<T> clazz) {
        JSONObject jsonobj = JSON.parseObject(jsontext);
        if (jsonobj == null) {
            return null;
        }
        Object obj = jsonobj.get(list_str);
        if (obj == null) {
            return null;
        }
        // if(obj instanceof JSONObject){}
        if (obj instanceof JSONArray) {
            JSONArray jsonarr = (JSONArray) obj;
            List<T> list = new ArrayList<T>();
            for (int i = 0; i < jsonarr.size(); i++) {
                list.add(jsonarr.getObject(i, clazz));
            }
            return list;
        }
        return null;
    }

    /**
     * @param <T>
     *            -> DepartmentBean
     * @param jsontext
     *            -> {"department":{"id":"1","name":"生产部"},"password":"admin",
     *            "username":"admin"}
     * @param obj_str
     *            -> department
     * @param clazz
     *            -> DepartmentBean
     * @return -> T
     */
    public static final <T> T getObject(String jsontext, String obj_str,
                                        Class<T> clazz) {
        JSONObject jsonobj = JSON.parseObject(jsontext);
        if (jsonobj == null) {
            return null;
        }

        Object obj = jsonobj.get(obj_str);
        if (obj == null) {
            return null;
        }

        if (obj instanceof JSONObject) {
            return jsonobj.getObject(obj_str, clazz);
        } else {
            logger.info(obj.getClass()+"");
        }

        return null;
    }

    /**
     * @param <T>
     * @param jsontext
     *            ->{"department":{"id":"1","name":"生产部"},"password":"admin",
     *            "username":"admin"}
     * @param clazz
     *            -> UserBean.class
     * @return -> UserBean
     */
    // 注：传入任意的jsontext,返回的T都不会为null,只是T的属性为null
    public static final <T> T getObject(String jsontext, Class<T> clazz) {
        T t = null;
        try {
            t = JSON.parseObject(jsontext, clazz);
        } catch (Exception e) {
            logger.error("json字符串转换失败！" + jsontext, e);
        }
        return t;
    }

    public static final String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * @Description： json字符串转成为List
     * @author: GuXiYang
     * @date: 2017/05/08 10:25:00
     * @param jsonStr
     *            json字符串
     * @param clazz
     *            class名称
     * @return
     */
    public static <T> List<T> getList(String jsonStr, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        try {
            list = JSON.parseArray(jsonStr, clazz);
        } catch (Exception e) {
            logger.error("json字符串转List失败！" + jsonStr, e);
        }
        return list;
    }

    /**
     *
     * @Description： json字符串转换成list<Map>
     * @author: GuXiYang
     * @date: 2017/05/08 10:27:16
     * @param jsonString
     *            json字符串
     * @return
     */
    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = JSON.parseObject(jsonString,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
        } catch (Exception e) {
            logger.error("json字符串转map失败", e);
        }
        return list;
    }

    /**
     * @Description： json字符串转换为Map
     * @author: GuXiYang
     * @date: 2017/05/08 10:32:33
     * @param jsonStr
     *            json字符串
     * @return
     */
    public static Map<String, Object> json2Map(String jsonStr) {
        try {
            return JSON.parseObject(jsonStr, Map.class);
        } catch (Exception e) {
            logger.error("json字符串转换失败！" + jsonStr, e);
        }
        return null;
    }



}