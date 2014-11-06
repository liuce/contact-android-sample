package org.lightips.sample.contacts.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    protected static final String TAG = MyService.class.getSimpleName();
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Service Start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    private Thread currentThread;


    /**
     * 进度条的最大值
     */
    public static final int MAX_PROGRESS = 1000;
    /**
     * 进度条的进度值
     */
    private int progress = 0;

    /**
     * 增加get()方法，供Activity调用
     * @return 下载进度
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 模拟下载任务，每秒钟更新一次
     */
    public void startDownLoad(){
        currentThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(progress < MAX_PROGRESS){
                    progress += 5;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        currentThread.start();
    }

    public void stopDownload(){
        currentThread.interrupt();
        currentThread = null;
    }

    public void resumeDownload(){
        this.progress = 0;
    }


    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public MyService getService(){
            return MyService.this;
        }
    }

}
