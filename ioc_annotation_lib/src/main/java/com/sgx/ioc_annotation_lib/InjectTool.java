package com.sgx.ioc_annotation_lib;

import android.util.Log;
import android.view.View;

import com.sgx.ioc_annotation_lib.annation_common.OnBaseCommon;
import com.sgx.ioc_annotation_lib.annotation.BindView;
import com.sgx.ioc_annotation_lib.annotation.ContentView;
import com.sgx.ioc_annotation_lib.annotation.OnClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectTool {
    private static final String TAG = InjectTool.class.getSimpleName();

    public static void inject(Object object) {
        injectSetContentView(object);
        injectBindView(object);
        injectOnClick(object);


        //通用适配事件代码
        injectEvent(object);
    }

    private static void injectEvent(Object object) {
        Class<?> mainClasses = object.getClass();
        Method[] declareMethods = mainClasses.getDeclaredMethods();
        for (Method declareMethod : declareMethods) {
            declareMethod.setAccessible(true);
            Annotation[] annotations = declareMethod.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                OnBaseCommon onBaseCommon = annotationType.getAnnotation(OnBaseCommon.class);
                if (null == onBaseCommon) {
                    Log.d(TAG, "onBaseCommon注解为null");
                    continue;
                }
                String setCommonListener = onBaseCommon.setCommonListener();
                Class setCommonObjectListener = onBaseCommon.setCommonObjectListener();
                String callbackMethod = onBaseCommon.callbackMethod();
                try {
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    valueMethod.setAccessible(true);
                    int value = (int) valueMethod.invoke(annotation);

                    Method findViewById = mainClasses.getMethod("findViewById", int.class);
                    View resultView = (View) findViewById.invoke(object, value);

                    Method mViewMethod = resultView.getClass().getMethod(setCommonListener, setCommonObjectListener);

                    //动态代理监听第三个要素
                    Object proxy = Proxy.newProxyInstance(setCommonListener.getClass().getClassLoader(),
                            new Class[]{setCommonObjectListener},
                            new InvocationHandler() {
                                @Override
                                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                                    return declareMethod.invoke(object, null);
                                }
                            });
                    mViewMethod.invoke(resultView, proxy);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


        }
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
