package com.albenw.validatorchain;

import com.albenw.validatorchain.base.Validator;
import com.albenw.validatorchain.base.ValidatorContext;

import java.util.List;

/**
 * @author alben.wong
 */
public class DefaultValidateCallback<T> implements ValidatorCallback<T> {

    @Override
    public void onSuccess(T target, List<Validator> validators, ValidatorContext context) {
    }

    @Override
    public void onFail(T target, List<Validator> validators, ValidatorContext context) {

    }

    @Override
    public void onException(T target, List<Validator> validators, ValidatorContext context, Exception e) {

    }

}
