package com.epam.dmivapi.timed;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class TimedAnnotationBeanPostProcessor implements BeanPostProcessor {
    private static final Logger logger = Logger.getLogger(TimedAnnotationBeanPostProcessor.class);
    private Map<String, Class> classMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Timed.class)) {
            classMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = classMap.get(beanName);
        if (beanClass == null)
            return bean;

        return Proxy.newProxyInstance(
                beanClass.getClassLoader(),
                beanClass.getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Instant start = Instant.now();
                        Object retVal = method.invoke(bean, args);
                        Instant finish = Instant.now();
                        logger.trace(
                                "Method: " +
                                method.getName() +
                                " finished in " +
                                Duration.between(start, finish).toMillis() +
                                " ms"
                        );
                        return retVal;
                    }
                });
    }
}
