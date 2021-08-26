package com.alipay.sofa.boot.autoconfigure.tracer;

import com.alipay.sofa.tracer.boot.kafka.processor.KafkaConsumerFactoryPostProcessor;
import com.alipay.sofa.tracer.boot.kafka.processor.KafkaProducerFactoryPostProcessor;
import com.sofa.alipay.tracer.plugins.kafkamq.aspect.KafkaListenerSofaTracerAspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;

/**
 *@author chenchen6  2020/9/2 21:56
 */

@Configuration
@AutoConfigureAfter(KafkaAutoConfiguration.class)
@ConditionalOnClass({ ProducerFactory.class, ConsumerFactory.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConditionalOnProperty(name = "com.alipay.tracer.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class SofaTracerKafkaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KafkaConsumerFactoryPostProcessor kafkaConsumerFactoryPostProcessor() {
        return new KafkaConsumerFactoryPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaProducerFactoryPostProcessor kafkaProducerFactoryPostProcessor() {
        return new KafkaProducerFactoryPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaListenerSofaTracerAspect kafkaListenerSofaTracerAspect() {
        return new KafkaListenerSofaTracerAspect();
    }
}