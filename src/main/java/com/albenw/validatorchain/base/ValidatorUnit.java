package com.albenw.validatorchain.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author alben.wong
 */
@Getter
@Setter
public class ValidatorUnit<T> {

    private T target;

    private Validator validator;

    public ValidatorUnit(T target, Validator validator){
        this.target = target;
        this.validator = validator;
    }

}
