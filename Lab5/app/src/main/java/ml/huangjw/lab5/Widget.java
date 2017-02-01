package ml.huangjw.lab5;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {
  private Map<String, Integer> map;

  public Widget() {
    map = new HashMap<>();
    map.put("Apple", R.mipmap.apple);
    map.put("Banana", R.mipmap.banana);
    map.put("Cherry", R.mipmap.cherry);
    map.put("Coco", R.mipmap.coco);
    map.put("Kiwi", R.mipmap.kiwi);
    map.put("Orange", R.mipmap.orange);
    map.put("Pear", R.mipmap.pear);
    map.put("Strawberry", R.mipmap.strawberry);
    map.put("Watermelon", R.mipmap.watermelon);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);
    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
    rv.setOnClickPendingIntent(R.id.widget_img, pi);
    appWidgetManager.updateAppWidget(appWidgetIds, rv);
  }

  @TargetApi(Build.VERSION_CODES.N)
  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    Bundle bundle = intent.getExtras();
    if (bundle != null && intent.getAction().equals("ml.huangjw.lab5.Widget")) {
      String fruit = bundle.getString("fruit");
      NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      Notification.Builder builder = new Notification.Builder(context);
      builder.setContentTitle("静态广播")
          .setContentText(fruit)
          .setPriority(Notification.PRIORITY_DEFAULT)
          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), map.get(fruit)))
          .setWhen(System.currentTimeMillis())
          .setSmallIcon(map.get(fruit))
          .setAutoCancel(true);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
      builder.setContentIntent(pendingIntent);
      Notification notification = builder.build();
      nm.notify(0, notification);

      RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
      rv.setTextViewText(R.id.widget_text, fruit);
      rv.setImageViewResource(R.id.widget_img, map.get(fruit));
      AppWidgetManager am = AppWidgetManager.getInstance(context);
      ComponentName cn = new ComponentName(context, Widget.class);
      am.updateAppWidget(cn, rv);
    }
  }
}