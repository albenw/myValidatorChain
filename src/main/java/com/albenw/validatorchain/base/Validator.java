package com.albenw.validatorchain.base;

/**
 * @author alben.wong
 */
public interface Validator<T> {

    /**
     * 是否符合条件校验
     * @param target
     * @param context
     * @return
     * @throws Exception
     */
    boolean accept(T target, ValidatorContext context) throws Exception;

    /**
     * 校验具体逻辑
     * @param target
     * @param context
     * @return
     * @throws Exception
     */
    boolean validate(T target, ValidatorContext context) throws Exception;

    /**
     * 校验过程中的异常处理
     * @param target
     * @param context
     * @param e
     * @return
     * @throws Exception
     */
    void onException(T target, ValidatorContext context, Exception e) throws Exception;

}
