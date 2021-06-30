package com.springmvc.framework.servlet;

import com.springmvc.framework.annotations.IAutowired;
import com.springmvc.framework.annotations.IController;
import com.springmvc.framework.annotations.IRequestMapping;
import com.springmvc.framework.annotations.IService;
import com.springmvc.framework.pojo.MyHandler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/28
 */
public class MyDispatcherServlet extends HttpServlet {

    // 存储配置文件地址
    private Properties properties = new Properties();

    // 存储class全路径名称
    private List<String> classNames = new ArrayList<>();

    // 实例化对象
    private Map<String, Object> instanceObjects = new HashMap<String, Object>();

    // 单例池对象
    private Map<String, Object> singletonObjects = new HashMap<String, Object>();

    // 映射处理器设置
    private List<MyHandler> handlerList = new ArrayList<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
        // 通过web.xml 获取资源配置文件
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);
        // 扫描类
        doScanPackage(properties.getProperty("package.name"));
        // 实例化
        doInstance();
        // 依赖注入
        doAutowired();
        // handlerMapping
        doHandlerMapping();

        System.out.println("lagou mvc 初始化完成....");
    }

    private void doHandlerMapping() {
        if (singletonObjects.isEmpty()) return;
        singletonObjects.forEach((k, v) -> {
            Class<?> aClass = v.getClass();
            if (aClass.isAnnotationPresent(IController.class)) {
                IRequestMapping aClassAnnotation = aClass.getAnnotation(IRequestMapping.class);
                String baseUrl = aClassAnnotation.value();
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    IRequestMapping methodAnnotation = method.getAnnotation(IRequestMapping.class);
                    if (methodAnnotation == null) continue;
                    String methodUrl = baseUrl + methodAnnotation.value();
                    MyHandler myHandler = new MyHandler(v, method, Pattern.compile(methodUrl));
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        if (parameters[i].getType() == HttpServletRequest.class || parameters[i].getType() == HttpServletResponse.class) {
                            myHandler.getParamsMap().put(parameters[i].getType().getSimpleName(), i);
                        } else {
                            myHandler.getParamsMap().put(parameters[i].getName(), i);
                        }
                    }
//                    handlerMap.put(methodUrl, myHandler);
                    handlerList.add(myHandler);
                }
            }
        });
    }

    private void doAutowired() {
        if (instanceObjects.isEmpty()) return;
        instanceObjects.forEach((k, v) -> {
            parseAutowired(k, v);
        });

    }

    private Object parseAutowired(String k, Object v) {
        Object targetObject = singletonObjects.get(k);
        if (targetObject != null) return targetObject;

        Class<?> aClass = v.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            IAutowired iAutowired = field.getAnnotation(IAutowired.class);
            if (iAutowired != null) {
                String fieldName = iAutowired.value();
                if (fieldName == null || fieldName.equals("")) {
                    fieldName = field.getName();
                }
                Object fieldObject = singletonObjects.get(fieldName);
                if (fieldObject == null) {
                    fieldObject = parseAutowired(fieldName, instanceObjects.get(fieldName));
                }
                field.setAccessible(true);
                try {
                    field.set(v, fieldObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        singletonObjects.put(k, v);
        return v;
    }


    private void doInstance() {
        if (classNames.size() == 0) {
            return;
        }
        classNames.forEach(c -> {
            try {
                Class<?> aClass = Class.forName(c);
                IController iController = aClass.getAnnotation(IController.class);
                IService iService = aClass.getAnnotation(IService.class);
                if (iController != null || iService != null) {
                    String iocKey = ((iController != null) ? iController.value() : iService.value());
                    // 当其注解没有配置时
                    if (iocKey == null || iocKey.equals("")) {
                        iocKey = lowerFirst(aClass.getSimpleName());
                    }
                    instanceObjects.put(iocKey, aClass.newInstance());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    private String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        if ('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    // 扫描类
    private void doScanPackage(String scanPackage) {
        String scanPackagePath = scanPackage.replaceAll("\\.", "/");
        // 通过当前线程获取包路径
        URL scanPackageUrl = Thread.currentThread().getContextClassLoader().getResource(scanPackagePath);
        File scanPackageDir = new File(scanPackageUrl.getFile());
        File[] files = scanPackageDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doScanPackage(scanPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                classNames.add(scanPackage + "." + file.getName().replaceAll(".class", ""));
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        MyHandler handler = getHandler(req);
        if (handler == null ){
            resp.getWriter().println("404");
            return;
        }

        // 获取所有参数类型数组，这个数组的长度就是我们最后要传入的args数组的长度
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();


        // 根据上述数组长度创建一个新的数组（参数数组，是要传入反射调用的）
        Object[] paraValues = new Object[parameterTypes.length];

        // 以下就是为了向参数数组中塞值，而且还得保证参数的顺序和方法中形参顺序一致

        Map<String, String[]> parameterMap = req.getParameterMap();

        // 遍历request中所有参数  （填充除了request，response之外的参数）
        for(Map.Entry<String,String[]> param: parameterMap.entrySet()) {
            // name=1&name=2   name [1,2]
            String value = StringUtils.join(param.getValue(), ",");  // 如同 1,2

            // 如果参数和方法中的参数匹配上了，填充数据
            if(!handler.getParamsMap().containsKey(param.getKey())) {continue;}

            // 方法形参确实有该参数，找到它的索引位置，对应的把参数值放入paraValues
            Integer index = handler.getParamsMap().get(param.getKey());//name在第 2 个位置

            paraValues[index] = value;  // 把前台传递过来的参数值填充到对应的位置去

        }


        int requestIndex = handler.getParamsMap().get(HttpServletRequest.class.getSimpleName()); // 0
        paraValues[requestIndex] = req;


        int responseIndex = handler.getParamsMap().get(HttpServletResponse.class.getSimpleName()); // 1
        paraValues[responseIndex] = resp;


        // 最终调用handler的method属性
        try {
            handler.getMethod().invoke(handler.getTargetObject(),paraValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private MyHandler getHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        for (MyHandler myHandler : handlerList) {
            if (myHandler.getPattern().matcher(requestURI).matches()) {
                return myHandler;
            }
        }
        return null;
    }

}
