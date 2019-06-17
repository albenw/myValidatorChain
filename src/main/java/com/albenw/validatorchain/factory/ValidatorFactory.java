package com.albenw.validatorchain.factory;

/**
 * @author alben.wong
 */
public interface ValidatorFactory {

    /**
     * 生成validator bean的方式
     * @param type
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T findByType(Class<T> type) throws Exception;

}
