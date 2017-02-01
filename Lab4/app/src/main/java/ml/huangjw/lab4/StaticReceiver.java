package ml.huangjw.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class StaticReceiver extends BroadcastReceiver {
  private Map<String, Integer> map;

  public StaticReceiver() {
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
  public void onReceive(Context context, Intent intent) {
    Bundle bundle = intent.getExtras();
    if (bundle != null) {
      String fruit = bundle.getString("fruit");
      NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      Notification.Builder builder = new Notification.Builder(context);
      builder.setContentTitle("静态广播")
          .setContentText(fruit)
          .setPriority(Notification.PRIORITY_DEFAULT)
          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),map.get(fruit)))
          .setWhen(System.currentTimeMillis())
          .setSmallIcon(map.get(fruit))
          .setAutoCancel(true);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
      builder.setContentIntent(pendingIntent);
      Notification notification = builder.build();
      nm.notify(0, notification);
    }
  }
}
