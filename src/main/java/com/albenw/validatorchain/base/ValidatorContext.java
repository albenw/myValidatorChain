package com.albenw.validatorchain.base;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alben.wong
 */
public class ValidatorContext {

    /**
     * 上下文存放的属性
     */
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    /**
     * 校验结果
     */
    private ValidatorResult result;

    public void addAttribute(String key, Object obj){
        attributes.put(key, obj);
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }

    public <T> T getAttribute(String key, Class<T> type){
        return (T) getAttribute(key);
    }

    public void appendError(String key, String errorMsg){
        result.addError(key, errorMsg);
    }

    public void setError(String key, List<String> errors){
        result.setError(key, errors);
    }

    public Boolean getValidateIsSuccess(){
        return result.getIsSuccess();
    }

    public void setResult(ValidatorResult result) {
        this.result = result;
    }

}
