package ml.huangjw.lab2_1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  String selected = "学生";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    addLoginListener();
    addRegisterListenr();
    addRadioBtnSelectListener();
  }

  private void addLoginListener() {
    Button login = (Button) findViewById(R.id.loginBtn);
    assert login != null;
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EditText usernameET = (EditText) findViewById(R.id.usernameET);
        EditText passwordET = (EditText) findViewById(R.id.passwordET);
        assert usernameET != null;
        assert passwordET != null;
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        if (username.trim().isEmpty())
          showToast(R.string.usernameerror);
        else if (password.isEmpty())
          showToast(R.string.passworderror);
        else {
          final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
          ad.setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              showToast(R.string.negative);
            }
          }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              showToast(R.string.positive);
            }
          }).create();
          if (username.equals("Android") && password.equals("123456"))
            ad.setMessage("登录成功");
          else
            ad.setMessage("登录失败");
          ad.show();
        }
      }
    });
  }

  private void addRegisterListenr() {
    Button registerBtn = (Button) findViewById(R.id.registerBtn);
    assert registerBtn != null;
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showToast(selected + "身份注册功能尚未开启");
      }
    });
  }

  private void addRadioBtnSelectListener() {
    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
    assert radioGroup != null;
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton checkBtn = (RadioButton) findViewById(checkedId);
        assert checkBtn != null;
        selected = checkBtn.getText().toString();
        showToast(selected + "身份被选中");
      }
    });
  }

  private void showToast(String msg) {
    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
  }
  private void showToast(int msg) {
    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
  }
}