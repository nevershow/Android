package ml.huangjw.lab8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_item);
    init();
  }

  private void init() {
    //隐藏年份
    final DatePicker dp = (DatePicker) findViewById(R.id.birthDate);
    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);

    final EditText nameEdit = (EditText) findViewById(R.id.nameEdit), giftEdit = (EditText) findViewById(R.id.giftEdit);
    Button btn = (Button) findViewById(R.id.addbtn);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String name = nameEdit.getText().toString(), gift = giftEdit.getText().toString();
        int month = dp.getMonth() + 1, day = dp.getDayOfMonth();

        if (name.isEmpty()) {
          showToast(R.string.emptyname);
        } else if (MainActivity.mydb.insert2DB(name, month, day, gift) == -1) {
          showToast(R.string.duplicatename);
        } else {
          Intent intent = new Intent();
          intent.putExtra("name", name)
              .putExtra("birthMon", Integer.toString(month))
              .putExtra("birthDay", Integer.toString(day))
              .putExtra("gift", gift);

          AddItemActivity.this.setResult(RESULT_OK, intent);
          AddItemActivity.this.finish();
        }
      }
    });
  }

  private void showToast(int str) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
  }
}
