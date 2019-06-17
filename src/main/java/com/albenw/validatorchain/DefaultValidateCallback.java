package com.albenw.validatorchain;

import com.albenw.validatorchain.base.ValidatorContext;
import com.albenw.validatorchain.base.ValidatorUnit;

import java.util.List;

/**
 * @author alben.wong
 */
public class DefaultValidateCallback implements ValidatorCallback {

    @Override
    public void onSuccess(List<ValidatorUnit> units, ValidatorContext context) {
    }

    @Override
    public void onFail(List<ValidatorUnit> units, ValidatorContext context) {

    }

    @Override
    public void onUncaughtException(List<ValidatorUnit> units, ValidatorContext context, Exception e) {

    }

}
