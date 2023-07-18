package com.sgx.ioc_annotation_lib;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class InjectTool {
    private static final String TAG = InjectTool.class.getSimpleName();

    public static void inject(Object object) {
        injectSetContentView(object);
        injectBindView(object);
        injectOnClick(object);
    }

    private static void injectOnClick(Object object) {
        Class<?> mainClasses = object.getClass();
        Method[] methods = mainClasses.getDeclaredMethods();
        for (Method declareMethod : methods) {
            declareMethod.setAccessible(true);
            OnClick onClick = declareMethod.getAnnotation(OnClick.class);
            if (null == onClick) {
                Log.d(TAG, "onClick注解为null");
                continue;
            }
            int viewId = onClick.value();
            try {
                Method findViewById = mainClasses.getMethod("findViewById", int.class);
                View resultView = (View) findViewById.invoke(object, viewId);
                resultView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            declareMethod.invoke(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectBindView(Object object) {
        Class<?> mainClasses = object.getClass();
        //获取contenview注解
        Field[] fields = mainClasses.getDeclaredFields();
        //获取contentview。value（）
        for (Field field : fields) {
            field.setAccessible(true);

            //关心bindView注解字段，其他不管
            BindView bindView = field.getAnnotation(BindView.class);
            if (null == bindView) {
                Log.d(TAG, "BindView注解为null");
                continue;
            }
            int viewId = bindView.value();
            try {
                Method findViewById = mainClasses.getMethod("findViewById", int.class);
                Object resultView = findViewById.invoke(object, viewId);
                field.set(object, resultView);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

    }

    private static void injectSetContentView(Object object) {
        Class<?> mainClasses = object.getClass();
        //获取contenview注解
        ContentView contentView = mainClasses.getAnnotation(ContentView.class);
        //获取contentview。value（）
        if (null == contentView) {
            Log.d(TAG, "contentView注解为null");
            return;
        }
        //获取R.layout布局文件
        int layoutId = contentView.value();
        //反射执行setContentView()
        try {
            Method setContentViewMethod = mainClasses.getMethod("setContentView", int.class);

            //通过method执行setContentView
            setContentViewMethod.invoke(object, layoutId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
