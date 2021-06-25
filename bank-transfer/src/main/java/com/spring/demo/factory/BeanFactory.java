package com.spring.demo.factory;

import com.spring.demo.annotation.Transaction;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/19
 */
public class BeanFactory {


    private static Map<String, Object> map = new HashMap<>();


    static {
        // 解析xml
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> beanElements = rootElement.selectNodes("//bean");
            for (int i = 0; i < beanElements.size(); i++) {
                Element element = beanElements.get(i);
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                Class<?> aClass = Class.forName(className);
                Object instance = aClass.newInstance();
                map.put(id, instance);
            }

            // 增强代码逻辑
            map.forEach((k, v) -> {
                Method[] methods = v.getClass().getMethods();
                for (int i = 0; i < methods.length; i++) {
                    Transaction declaredAnnotation = methods[i].getDeclaredAnnotation(Transaction.class);
                    if (declaredAnnotation != null) {
                        Object proxyObject = ((ProxyFactory) map.get("proxyFactory")).getJdkProxyObject(v);
                        map.put(k,proxyObject);
                    }
                }
            });


            List<Element> propertyElements = rootElement.selectNodes("//property");
            for (int i = 0; i < propertyElements.size(); i++) {
                Element element = propertyElements.get(i);

                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                Element parent = element.getParent();
                String parentId = parent.attributeValue("id");
                Object parentInstance = map.get(parentId);

                Method[] methods = parentInstance.getClass().getMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    if (method.getName().equalsIgnoreCase("set" + name)) {
                        method.invoke(parentInstance, map.get(ref));
                    }
                }
                map.put(parentId, parentInstance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Object getBean(String id) {
        return map.get(id);
    }
}
