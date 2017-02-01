package ml.huangjw.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private boolean hasPassword;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SharedPreferences sp = getSharedPreferences("Lab7", MODE_PRIVATE);
    hasPassword = sp.getBoolean("hasPassword", false);
    init();
  }

  private void init() {
    final EditText et1 = (EditText) findViewById(R.id.password), et2 = (EditText) findViewById(R.id.confirm);
    Button ok = (Button) findViewById(R.id.ok), clear = (Button) findViewById(R.id.clear);

    if (hasPassword) {
      et1.setVisibility(View.INVISIBLE);
      et2.setHint(R.string.hint3);
    }

    ok.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!hasPassword) {
          if (et1.getText().toString().isEmpty())
            showToast(R.string.error2);
          else if (!et1.getText().toString().equals(et2.getText().toString()))
            showToast(R.string.error1);
          else {
            SharedPreferences sp = getSharedPreferences("Lab7", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("hasPassword", true);
            editor.putString("password", et2.getText().toString());
            editor.apply();
            startActivity(new Intent(MainActivity.this, FileEditActivity.class));
          }
        }

        else {
          SharedPreferences sp = getSharedPreferences("Lab7", MODE_PRIVATE);
          String password = sp.getString("password", "");
          if (!et2.getText().toString().equals(password))
            showToast(R.string.error3);
          else
            startActivity(new Intent(MainActivity.this, FileEditActivity.class));
        }
      }
    });

    clear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        et1.setText("");
        et2.setText("");
      }
    });
  }

  private void showToast(int str) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
  }
}
