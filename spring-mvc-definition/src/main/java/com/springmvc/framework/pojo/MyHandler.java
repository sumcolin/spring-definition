package com.springmvc.framework.pojo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/28
 */
public class MyHandler {

    private Pattern pattern;

    private Object targetObject;

    private Method method;

    private Map<String, Integer>  paramsMap;

    public MyHandler(Object targetObject, Method method, Pattern pattern) {
        this.targetObject = targetObject;
        this.method = method;
        this.pattern = pattern;
        this.paramsMap = new HashMap<>();
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, Integer> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, Integer> paramsMap) {
        this.paramsMap = paramsMap;
    }
}
