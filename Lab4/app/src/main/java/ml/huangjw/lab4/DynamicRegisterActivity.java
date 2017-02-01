package ml.huangjw.lab4;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DynamicRegisterActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dynamic_layout);
    init();
  }

  private void init() {
    final DynamicReceiver dynamicReceiver = new DynamicReceiver();
    final Button broadcast = (Button) findViewById(R.id.broadcast);
    broadcast.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (broadcast.getText().toString().equals("Register Broadcast")) {
          broadcast.setText(R.string.UnregisterBroadcast);
          registerReceiver(dynamicReceiver, new IntentFilter("ml.huangjw.lab4.dynamicrecevier"));
        } else {
          broadcast.setText(R.string.RegisterBroadcast);
          unregisterReceiver(dynamicReceiver);
        }
      }
    });

    Button send = (Button) findViewById(R.id.send);
    send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EditText msg = (EditText) findViewById(R.id.msg);
        sendBroadcast(new Intent("ml.huangjw.lab4.dynamicrecevier").putExtra("msg", msg.getText().toString()));
      }
    });
  }
}
