package com.albenw.validatorchain;

import com.albenw.validatorchain.base.Validator;
import com.albenw.validatorchain.factory.ValidatorFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alben.wong
 */
public class ValidatorCache {

    /**
     * Validator的缓存
     * key为类名
     */
    private static Map<Class<? extends Validator>, Validator> VALIDATOR_CACHE_MAP = new ConcurrentHashMap<>();

    public static Validator getByType(ValidatorFactory factory, Class<? extends Validator> type) throws Exception {
        synchronized (VALIDATOR_CACHE_MAP){
            if(VALIDATOR_CACHE_MAP.containsKey(type)){
                return VALIDATOR_CACHE_MAP.get(type);
            }
            Validator validator = factory.findByType(type);
            VALIDATOR_CACHE_MAP.putIfAbsent(type, validator);
            return validator;
        }
    }

}
