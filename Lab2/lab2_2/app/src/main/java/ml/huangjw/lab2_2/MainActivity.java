package ml.huangjw.lab2_2;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
  String selected = "学生";
  LinearLayout rootview;
  TextInputLayout username;
  TextInputLayout password;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    rootview = (LinearLayout) findViewById(R.id.rootview);
    username = (TextInputLayout) findViewById(R.id.username);
    password = (TextInputLayout) findViewById(R.id.password);
    addLoginListener();
    addRegisterListener();
    addRadioBtnSelectListener();
    addEditTextClickListener();
  }

  private void addLoginListener() {
    Button login = (Button) findViewById(R.id.loginBtn);
    assert login != null;
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EditText usernameET = username.getEditText();
        EditText passwordET = password.getEditText();
        assert usernameET != null;
        assert passwordET != null;
        String user = usernameET.getText().toString();
        String pass = passwordET.getText().toString();
        if (user.trim().isEmpty()) {
          username.setError("用户名不能为空");
          password.setErrorEnabled(false);
        }
        else if (pass.isEmpty()) {
          password.setError("密码不能为空");
          username.setErrorEnabled(false);
        }
        else {
          username.setErrorEnabled(false);
          password.setErrorEnabled(false);
          if (user.equals("Android") && pass.equals("123456"))
            showSnackbar("登录成功");
          else
            showSnackbar("登录失败");
        }
      }
    });
  }

  private void addRegisterListener() {
    Button registerBtn = (Button) findViewById(R.id.registerBtn);
    assert registerBtn != null;
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showSnackbar(selected + "身份注册功能尚未开启");
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
        showSnackbar(selected + "身份被选中");
      }
    });
  }

  private void addEditTextClickListener() {
    EditText usernameET = username.getEditText();
    EditText passwordET = password.getEditText();
    assert usernameET != null;
    assert passwordET != null;
    usernameET.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        username.setHint("请输入用户名");
      }
    });
    passwordET.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        password.setHint("请输入密码");
      }
    });
  }

  private void showSnackbar(String msg) {
    Snackbar.make(rootview, msg, 5000).setAction("确定", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(MainActivity.this, R.string.snackbarclick, Toast.LENGTH_SHORT).show();
      }
    }).setActionTextColor(getResources().getColor(R.color.colorPrimary))
    .show();
  }
}