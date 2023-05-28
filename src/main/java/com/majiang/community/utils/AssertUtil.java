package com.majiang.community.utils;

import com.majiang.community.exceptions.ParamsException;

/**
 * Author wxh
 * 2023/3/4 14:49
 */
public class AssertUtil {
    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw  new ParamsException(msg);
        }
    }
}
