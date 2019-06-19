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
    private static Map<String, Validator> VALIDATOR_CACHE_MAP = new ConcurrentHashMap<>();

    public static Validator getByType(ValidatorFactory factory, Class<? extends Validator> type) throws Exception {
        String typeName = type.getName();
        synchronized (VALIDATOR_CACHE_MAP){
            if(VALIDATOR_CACHE_MAP.containsKey(typeName)){
                return VALIDATOR_CACHE_MAP.get(typeName);
            }
            Validator validator = factory.findByType(type);
            VALIDATOR_CACHE_MAP.putIfAbsent(typeName, validator);
            return validator;
        }
    }

}
