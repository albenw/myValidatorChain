package com.albenw.validatorchain;

import com.albenw.validatorchain.base.Validator;
import com.albenw.validatorchain.base.ValidatorContext;

import java.util.List;

/**
 * @author alben.wong
 */
public interface ValidatorCallback<T> {

    /**
     * 每次校验成功的回调
     * @param validators
     * @param context
     */
    void onSuccess(T target, List<Validator> validators, ValidatorContext context);

    /**
     * 每次校验失败的回调
     * @param validators
     * @param context
     */
    void onFail(T target, List<Validator> validators, ValidatorContext context);

    /**
     * 校验时发生异常的回调
     * @param validators
     * @param context
     * @param e
     */
    void onException(T target, List<Validator> validators, ValidatorContext context, Exception e);

}
