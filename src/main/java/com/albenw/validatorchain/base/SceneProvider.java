package com.albenw.validatorchain.base;

import java.util.List;

/**
 * @author alben.wong
 */
public interface SceneProvider<T> {

    List<String> decide(T target);

}
