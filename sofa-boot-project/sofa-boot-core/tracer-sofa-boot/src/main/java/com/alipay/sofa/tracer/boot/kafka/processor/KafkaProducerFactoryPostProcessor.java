package com.alipay.sofa.tracer.boot.kafka.processor;

import com.sofa.alipay.tracer.plugins.kafkamq.factories.SofaTracerKafkaProducerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.kafka.core.ProducerFactory;

/**
 * KafkaProducerFactoryPostProcessor。
 *
 * @author chenchen6   2020/9/3 22:15
 *
 */
public class KafkaProducerFactoryPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof ProducerFactory && !(bean instanceof SofaTracerKafkaProducerFactory)) {
            return new SofaTracerKafkaProducerFactory((ProducerFactory) bean);
        }
        return bean;
    }
}