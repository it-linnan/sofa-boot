/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.healthcheck.impl;

import com.alipay.sofa.boot.constant.SofaBootConstants;
import com.alipay.sofa.healthcheck.core.HealthChecker;
import com.alipay.sofa.isle.ApplicationRuntimeModel;
import com.alipay.sofa.isle.deployment.DeploymentDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * Abstract Module Health Checker
 *
 * @author xuanbei 18/5/16
 */
public class ModuleHealthChecker implements ApplicationContextAware, HealthChecker {

    private int                retryCount;
    private long               retryInterval;
    private boolean            strictCheck;
    private int                timeout;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Environment environment = applicationContext.getEnvironment();
        this.retryCount = Integer.parseInt(environment.getProperty(
            SofaBootConstants.SOFABOOT_MODULE_CHECK_RETRY_COUNT,
            String.valueOf(SofaBootConstants.SOFABOOT_MODULE_CHECK_RETRY_DEFAULT_COUNT)));
        this.retryInterval = Integer.parseInt(environment.getProperty(
            SofaBootConstants.SOFABOOT_MODULE_CHECK_RETRY_INTERVAL,
            String.valueOf(SofaBootConstants.SOFABOOT_MODULE_CHECK_RETRY_DEFAULT_INTERVAL)));
        this.strictCheck = Boolean.parseBoolean(environment.getProperty(
            SofaBootConstants.SOFABOOT_MODULE_CHECK_STRICT_ENABLED,
            String.valueOf(SofaBootConstants.SOFABOOT_MODULE_CHECK_STRICT_DEFAULT_ENABLED)));
        this.timeout = Integer.parseInt(environment.getProperty(
            SofaBootConstants.SOFABOOT_MODULE_HEALTH_CHECK_TIMEOUT,
            String.valueOf(SofaBootConstants.SOFABOOT_MODULE_HEALTH_CHECK_DEFAULT_TIMEOUT)));
    }

    @Override
    public Health isHealthy() {
        Health.Builder builder = new Health.Builder();
        ApplicationRuntimeModel application = applicationContext.getBean(
            SofaBootConstants.APPLICATION, ApplicationRuntimeModel.class);

        for (DeploymentDescriptor deploymentDescriptor : application.getFailed()) {
            builder.withDetail(deploymentDescriptor.getName(), "failed");
        }

        if (application.getFailed().size() == 0) {
            return builder.status(Status.UP).build();
        } else {
            return builder.status(Status.DOWN).build();
        }
    }

    @Override
    public String getComponentName() {
        return "SOFABoot-Modules";
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public long getRetryTimeInterval() {
        return retryInterval;
    }

    @Override
    public boolean isStrictCheck() {
        return strictCheck;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }
}
