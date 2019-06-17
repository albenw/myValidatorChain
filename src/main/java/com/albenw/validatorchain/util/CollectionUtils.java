package com.albenw.validatorchain.util;

import java.util.Collection;

/**
 * @author alben.wong
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

}
