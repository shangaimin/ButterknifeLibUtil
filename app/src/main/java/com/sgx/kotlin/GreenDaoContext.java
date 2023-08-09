package com.sgx.kotlin;

import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class GreenDaoContext extends ContextWrapper {
    public GreenDaoContext() {
        super(MyApplication.getmContext());
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象
     *
     * @param dbName
     */
    @Override
    public File getDatabasePath(String dbName) {
        String dbDir = getLogFile().getAbsolutePath();
        File baseFile = new File(dbDir);
        // 目录不存在则自动创建目录
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        //如果需要建立多个库，根据用户之类建立文件夹自行处理
        StringBuffer buffer = new StringBuffer();
        buffer.append(baseFile.getPath());
        dbDir = buffer.toString();
        buffer.append(File.separator);
        buffer.append(dbName);
        String dbPath = buffer.toString();
        // 判断目录是否存在，不存在则创建该目录
        File dirFile = new File(dbDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        // 数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        // 判断文件是否存在，不存在则创建该文件
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
            } catch (IOException e) {
                Log.e("test","");;
            }
        } else {
            isFileCreateSuccess = true;
        }
        //返回数据库文件对象
        if (isFileCreateSuccess) {
            return dbFile;
        } else {
            return super.getDatabasePath(dbName);
        }
    }

    private static File getLogFile() {
        File file;
        if (Environment.getExternalStorageState().equals("mounted")) {
            file = new File(MyApplication.getmContext().getExternalFilesDir("database").getPath() + "/");
        } else {
            file = new File(MyApplication.getmContext().getFilesDir().getPath() + "/database/");
        }

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see ContextWrapper#openOrCreateDatabase(String, int,
     * SQLiteDatabase.CursorFactory,
     * DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
    }
}
