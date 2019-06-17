package com.albenw.validatorchain.base;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alben.wong
 */
@Getter @Setter
public class ValidatorResult {

    /**
     * 是否成功
     */
    private Boolean isSuccess = true;

    /**
     * 错误信息
     */
    private Map<String, List<String>> errors = new ConcurrentHashMap<>();

    public void addError(String key, String errorMsg){
        if(!errors.containsKey(key)){
            errors.putIfAbsent(key, new LinkedList<>());
        }
        errors.get(key).add(errorMsg);
    }

    public void setError(String key, List<String> list){
        errors.putIfAbsent(key, list);
    }

}
