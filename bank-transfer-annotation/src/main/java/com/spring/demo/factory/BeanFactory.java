package com.spring.demo.factory;

import com.spring.demo.annotation.IAutowired;
import com.spring.demo.annotation.IComponent;
import com.spring.demo.annotation.IService;
import com.spring.demo.annotation.ITransaction;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/19
 */
public class BeanFactory {


    // 完成时
    @Deprecated
    private static Map<String, Object> map = new HashMap<>();

    // 完成时/ 已完成属性装配与代理对象生成
    private static Map<String, Object> singletonObjects = new HashMap<>();

    // 早期/ 只是进行了实例化操作
    private static Map<String, Object> earlyObjects = new HashMap<>();


    static {
        // 1、通过获取资源文件的方式获取bean对象, 并进行依赖处理
        String[] packageNames = {"com.spring.demo.utils", "com.spring.demo.service", "com.spring.demo.dao", "com.spring.demo.factory"};
        // 资源文件获取
        List<Class> classList = getClasses(packageNames);
        // 2、根据注解筛选, 获取实例化对象
        classList.forEach(c -> {
            IComponent iComponent = (IComponent) c.getAnnotation(IComponent.class);
            IService iService = (IService) c.getAnnotation(IService.class);
            if (iComponent != null || iService != null) {
                try {
                    Object instance = c.newInstance();
                    String simpleName = (iComponent != null ? ((IComponent) iComponent).value() : ((IService) iService).value());
                    earlyObjects.put(simpleName, instance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        // 3、属性装配与代码逻辑增强
        earlyObjects.forEach((k, v) -> {
            parseFiled(k, v);
        });
        System.out.println();
    }

    /**
     * 属性装配与代码逻辑增强
     */
    private static Object parseFiled(String k, Object v) {
        // 因为遍历时，可能存在已经完成单例对象，不要重复操作。
        Object singletonObject = singletonObjects.get(k);
        if (singletonObject != null) {
            return singletonObject;
        }
        // 属性填充
        Field[] declaredFields = v.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            IAutowired iAutowired = field.getAnnotation(IAutowired.class);
            if (iAutowired != null) {
                field.setAccessible(true);
                String targetKey = iAutowired.value();
                // 单例池未发现完成时状态的对象
                Object targetObject = singletonObjects.get(targetKey);
                if (targetObject == null) {
                    targetObject = parseFiled(targetKey, earlyObjects.get(targetKey));
                }
                try {
                    field.set(v, targetObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        // 逻辑代码增强配置
        Method[] methods = v.getClass().getDeclaredMethods();
        boolean isProxy = false;
        for (Method method : methods) {
            boolean annotationPresent = method.isAnnotationPresent(ITransaction.class);
            if (annotationPresent){
                isProxy = true;
                break;
            }
        }
        // 逻辑代码增强操作
        if (isProxy && !k.equals("proxyFactory")) {
            // 单例池中没有proxyFactory那我们会重新创建对象
            Object targetObject = singletonObjects.get("proxyFactory");
            if (targetObject == null) {
                targetObject = parseFiled("proxyFactory", earlyObjects.get("proxyFactory"));
            }
            // 通过该类是否集成了接口
            Class<?>[] interfaces = v.getClass().getInterfaces();
            if (interfaces != null && interfaces.length>0) {
                v = ((ProxyFactory) targetObject).getJdkProxyObject(v);
            }else {
                v = ((ProxyFactory) targetObject).getCglibProxyObject(v);
            }
        }
        singletonObjects.put(k, v);
        return v;
    }

    /**
     * 获取class文件
     * 多目录加载
     *
     * @param packageNames 包名
     * @return
     */
    private static List<Class> getClasses(String[] packageNames) {

        List<Class> classList = new ArrayList<>();
        File dirPackage = null;
        try {
            for (String packageName : packageNames) {
                String packageSourceName = packageName.replace(".", "/");
                // 通过当前线程获取上下文， 并获取资源文件绝对路径
                URL packageUrl = Thread.currentThread().getContextClassLoader().getResource(packageSourceName);
                dirPackage = new File(packageUrl.getFile());
                classList.addAll(findClass(dirPackage, packageName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    private static List<Class> findClass(File dirFile, String packageName) {
        List<Class> classList = new ArrayList<>();
        File[] files = dirFile.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                classList.addAll(findClass(f, packageName + "." + f.getName()));
            } else if (f.getName().endsWith(".class")) {
                String className = packageName + "." + f.getName().substring(0, f.getName().length() - 6);
                System.out.println(className);
                try {
                    classList.add(Class.forName(className));
                } catch (Exception e) {
                    System.out.println("nihao");
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return classList;
    }

    public static Object getBean(String id) {
        return singletonObjects.get(id);
    }

    /**
     * 解析xml方式获取bean对象
     */
    private static void paresXmlBeans() {
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
                    ITransaction declaredAnnotation = methods[i].getDeclaredAnnotation(ITransaction.class);
                    if (declaredAnnotation != null) {
                        Object proxyObject = ((ProxyFactory) map.get("proxyFactory")).getJdkProxyObject(v);
                        map.put(k, proxyObject);
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
}
