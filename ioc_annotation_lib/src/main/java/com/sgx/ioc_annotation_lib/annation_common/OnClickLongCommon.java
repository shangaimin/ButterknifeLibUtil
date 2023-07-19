package com.sgx.ioc_annotation_lib.annation_common;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@OnBaseCommon(setCommonListener = "setOnLongClickListener",
        setCommonObjectListener = View.OnLongClickListener.class,
        callbackMethod = "onClickLong")
public @interface OnClickLongCommon {
    int value() default -1;
}
