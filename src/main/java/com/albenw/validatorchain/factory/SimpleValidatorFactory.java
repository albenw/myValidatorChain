package com.albenw.validatorchain.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alben.wong
 */
public class SimpleValidatorFactory implements ValidatorFactory {

    private static Logger logger = LoggerFactory.getLogger(SimpleValidatorFactory.class);

    @Override
    public <T> T findByType(Class<T> type) throws Exception {
        if(type == null){
            throw new NullPointerException("tyep can not be null");
        }
        try{
            return type.newInstance();
        }catch (Exception e){
            logger.error("new a validator exception", e);
            throw e;
        }
    }
}
