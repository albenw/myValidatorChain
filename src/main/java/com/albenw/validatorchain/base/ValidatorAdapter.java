package com.albenw.validatorchain.base;

/**
 * @author alben.wong
 */
public class ValidatorAdapter<T> implements Validator<T>{

    @Override
    public boolean accept(T target, ValidatorContext context) throws Exception {
        return true;
    }

    @Override
    public boolean validate(T target, ValidatorContext context) throws Exception {
        return false;
    }

    @Override
    public void onException(T target, ValidatorContext context, Exception e) throws Exception {
        throw e;
    }

}
