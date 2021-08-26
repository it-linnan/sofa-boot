package com.alipay.sofa.tracer.boot.kafka.processor;

import com.sofa.alipay.tracer.plugins.kafkamq.factories.SofaTracerKafkaConsumerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.kafka.core.ConsumerFactory;

/**
 * KafkaConsumerFactoryPostProcessor.
 *
 * @author chenchen6   2020/9/3 22:18
 *
 */
public class KafkaConsumerFactoryPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof ConsumerFactory && !(bean instanceof SofaTracerKafkaConsumerFactory)) {
            return new SofaTracerKafkaConsumerFactory((ConsumerFactory) bean);
        }
        return bean;
    }
}