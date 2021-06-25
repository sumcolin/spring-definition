package com.spring.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.demo.pojo.Result;

/**
 * @auth Joeyzz7000
 * @email 741779841@qq.com
 * @date 2021/6/17
 */
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    // 对象转换为json字符串
    public static String object2Json(Result data) {
        try {
            String dataStr = MAPPER.writeValueAsString(data);
            return dataStr;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
