package com.sgx.kotlin;

import java.util.List;

public class GreenUtils {
    public static List<JPushMessages> loadAll(){
        return MyApplication.getmDaoSession().getJPushMessagesDao().queryBuilder()
                .where(JPushMessagesDao.Properties.State.eq("0"))
                .orderDesc(JPushMessagesDao.Properties.Time).list();
    }
    public static void clearAllMessage(JPushMessages jPushMessages){
        jPushMessages.setState(1);
        MyApplication.mDaoSession.getJPushMessagesDao().insertOrReplace(jPushMessages);

    }
}
