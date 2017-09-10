package com.snail.appwidgetdemo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by yongjie on 2017/9/9.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {
    private static final String LYJ_TAG = "LYJ_MyAppWidgetProvider";

    //第三个参数：appWidgetIds 每个AppWidget实例都有一个id，整个主要是标记是哪个AppWidget实例
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LYJ_TAG, "onUpdate");
        //加载布局 将整个AppWidget布局包装在RemoteViews（因为只是跨进程的）
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.exampel_appwidget);
        //给AppWidget布局中Button对象（next Button）添加监听器并指定其点击行为（点击启动第二个Activity）
        Intent startSecondActivity = new Intent(context, SecondActivity.class);
        PendingIntent secondActivity = PendingIntent.getActivity(context, 0, startSecondActivity, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn_appwidget_preview, secondActivity);

        //给AppWidget布局中Button对象（preview Button）添加监听器并指定点击行为（发送一个广播）
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.main.intent");
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 1, broadcastIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn_appwidget_next, broadcast);
        //AppWidgetManager更新AppWidget
        appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //调用父类的方法用于处理AppWidget的广告时间 比如onUpdate()
        super.onReceive(context, intent);
        //处理我们自己的自定义广播事件
        if ("com.main.intent".equals(intent.getAction())) {
            Log.d("LYJ", "com.main.intent");
            Toast.makeText(context, "hello world", Toast.LENGTH_LONG).show();
            //加载AppWidget布局
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.exampel_appwidget);
            //给AppWidget中的textView对象设置文本
            remoteViews.setTextViewText(R.id.text_view_appwidget_content, "DOg");
            //获取管理者对象
            AppWidgetManager instance = AppWidgetManager.getInstance(context);
            //管理者负责更新AppWidget
            instance.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);

        }
    }
}
