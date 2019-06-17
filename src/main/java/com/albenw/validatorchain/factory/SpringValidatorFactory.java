package com.albenw.validatorchain.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author alben.wong
 */
@Component
public class SpringValidatorFactory implements ValidatorFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public <T> T findByType(Class<T> type) throws Exception {
        return applicationContext.getBean(type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
