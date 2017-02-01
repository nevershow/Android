package ml.huangjw.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class DynamicReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("ml.huangjw.lab4.dynamicrecevier")) {
      Bundle bundle = intent.getExtras();
      if (bundle != null) {
        String msg = bundle.getString("msg");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("动态广播")
            .setContentText(msg)
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.dynamic))
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.dynamic)
            .setAutoCancel(true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        nm.notify(0, notification);
      }
    }
  }
}
