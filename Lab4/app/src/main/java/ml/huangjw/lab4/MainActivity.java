package ml.huangjw.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  Button btn1, btn2;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btn1 = (Button) findViewById(R.id.static_register);
    btn2 = (Button) findViewById(R.id.dynamic_register);
    btn1.setOnClickListener(this);
    btn2.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v == btn1) startActivity(new Intent(MainActivity.this, StaticRegisterActivity.class));
    else startActivity(new Intent(MainActivity.this, DynamicRegisterActivity.class));
  }
}
