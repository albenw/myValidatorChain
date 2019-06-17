package com.albenw.validatorchain;

import com.albenw.validatorchain.base.ValidatorContext;
import com.albenw.validatorchain.base.ValidatorUnit;

import java.util.List;

/**
 * @author alben.wong
 */
public interface ValidatorCallback {

    /**
     * 每次校验成功的回调
     * @param units
     * @param context
     */
    void onSuccess(List<ValidatorUnit> units, ValidatorContext context);

    /**
     * 每次校验失败的回调
     * @param units
     * @param context
     */
    void onFail(List<ValidatorUnit> units, ValidatorContext context);

    /**
     * 校验时发生异常的回调
     * @param units
     * @param context
     * @param e
     */
    void onUncaughtException(List<ValidatorUnit> units, ValidatorContext context, Exception e);

}
