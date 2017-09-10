##AppWidget开发步骤：
```
第一步：
画AppWidget 呈现的布局效果（AppWidget支持的view大多比较简单，但是基本是满足我们的需求）
因为AppWidget定位就是提供一些简单地快捷操作）
对应项目的 -->layout/exampel_appwidget.xml
```

```
第二步：配置AppWidget 在res/xml/xxxx.xml
标准代码格式：
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:initialLayout="@layout/exampel_appwidget"//指定第一步的布局文件
    android:minHeight="100dp"//指定最小显示高度
    android:minWidth="200dp"
    android:previewImage="@mipmap/ic_launcher"//指定预览图片（就是用户选择AppWidget预览样式）
    android:resizeMode="none"//配置AppWidget是否可以被用户拉伸（大部分就不给用户拉伸）
    android:updatePeriodMillis="10000"//AppWidget 定时发送广播调用onUpdata（）方法的时间。这个设置10s目前没有效果，可能是10s太短，系统不会按这个时间给我们调用onUpdate方法
    android:widgetCategory="home_screen">//高Android版本就只支持这一个值了
</appwidget-provider>

这个xml定义 和 AppWidgetProviderInfo 产生映射关系。系统会根据这个xml去构建对象。所以其他的配置属性
可以在这个对象中进行查找

```

```
第三步：
自定义一个provider去继承AppWidgetProvider(这是广播子类)
在清单文件中进行注册
标准格式：
 <receiver android:name=".MyAppWidgetProvider">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />//这个action对应onUpdate方法
                <action android:name="com.main.intent" />
            </intent-filter>
            <meta-data//必须要有
                android:name="android.appwidget.provider"//固定值
                android:resource="@xml/example_appwidget_info" />//指定第二步xml的AppWidget配置属性
 </receiver>
 注意：我们重写AppWidgetProvider的一些方法比如：onEnabled(Context context)方法，因为AppWidget是基于广播驱动的，所以每个方
 法都有对应的广播action都需要在<receiver/>中指定不然无法接受到。onEnabled对应的Action android.appwidget.action.APPWIDGET_ENABLED
 这些Action都在AppWidgetManager中定义了
```
至此运行代码就可以看到我们的AppWidget了

##补充说明
```
AppWidget是跨进程操作
开发AppWidget核心类：
AppWidgetProviderInfo：隐式的，系统很根据xml帮我们构建并序列化进行传递
AppWidgetProvider:广播，基于广播驱动的
-onUpdate():用户添加AppWidget实例时、定时都会触发（AppWidget在用户添加是可以启动一个Activity来配置AppWidget,如果配置了这个Activity,那么当用户添加AppWidget就不会触发该方法。但随后的定时还是会触发）
-onEnabled():用户添加第一个AppWidget实例时调用
-onDisEnabled()：用户删除最后一个AppWidget时调用
-xxx其他方法都可以在类中查看。以上每个方法要想被调用都要注册Action
AppWidgetManager:管理AppWidget.比如像更新AppWidget的UI最后都需要调用updateAppWidget（xx）有很多的重载方法
RemoteViews:负责加载AppWidget对于的布局，然后给布局中view设置监听器、给textView设置文本、给ImageView设置图片操作等等。但最后都必须调用AppWidgetManager的updateAppWidget（xx）来生效更新
```

##AppWidget 和 App进行通信
通信是跨进程的
```
比如AppWidget上有一个Button按钮我们可以给这个Button设置一个广播事件，每次点击都会发送一个广播，
这样我们在AppWidgetProvider中onReceiver中接受这个广播然后可以完成一些操作。
```