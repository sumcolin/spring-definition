package com.spring.demo.factory;

import com.spring.demo.annotation.IAutowired;
import com.spring.demo.annotation.IComponent;
import com.spring.demo.annotation.ITransaction;
import com.spring.demo.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/19
 */
@IComponent(value = "proxyFactory")
public class ProxyFactory {

    @IAutowired(value = "transactionManager")
    private TransactionManager transactionManager;

    public Object getJdkProxyObject(Object object) {

        Object proxyObject = Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object invoke = null;
                try {
                    transactionManager.beginTransaction();
                    invoke = method.invoke(object, args);
                    transactionManager.commit();
                }catch (Exception e){
                    transactionManager.rollback();
                    throw e;
                }
                return invoke;
            }
        });
        return proxyObject;
    }


    public Object getCglibProxyObject(Object object) {

        Object proxyObject = Enhancer.create(object.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object invoke = null;
                try {
                    transactionManager.beginTransaction();
                    invoke = method.invoke(o, objects);
                    transactionManager.commit();
                }catch (Exception e){
                    transactionManager.rollback();
                    throw e;
                }
                return invoke;
            }
        });
        return proxyObject;
    }

}
