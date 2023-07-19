package com.sgx.ioc_annotation_lib.annation_common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnBaseCommon {
    //事件三要素
    //1 订阅方式 setOnCLickListener  setOnLongClicklListener setOnDragListener
    String setCommonListener();

    //2  事件源对象   View.OnClickListener  View.setOnLongClicklListener 一般都是一个函数
    Class setCommonObjectListener();

    //3  事件执行方法   onClick onLongClick  最终的事件消费
    String callbackMethod();
}
