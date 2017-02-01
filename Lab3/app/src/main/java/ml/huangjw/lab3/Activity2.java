package ml.huangjw.lab3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Huangjw on 2016/10/15.
 */
public class Activity2 extends AppCompatActivity {
  Map<String, String[]> info;
  String name;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity2);
    Bundle extras = getIntent().getExtras();
    name = extras.getString("name");
    init();
    addOperation();
    addClickListener();
  }

  private void init() {
    info = new HashMap<>();
    InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.list));
    BufferedReader reader = new BufferedReader(inputStreamReader);
    try {
      String line = reader.readLine();
      while (line != null) {
        String [] arr =line.split(" ");
        info.put(arr[0], arr);
        line = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    String [] user = (info.get(name));
    TextView phoneNum = (TextView) findViewById(R.id.phoneNum),
        phoneDetail = (TextView) findViewById(R.id.phoneDetail),
        nametv = (TextView) findViewById(R.id.name);
    assert nametv != null;
    nametv.setText(user[0]);
    assert phoneNum != null;
    phoneNum.setText(user[1]);
    assert phoneDetail != null;
    phoneDetail.setText("手机  " + user[3]);
    RelativeLayout top = (RelativeLayout) findViewById(R.id.topDiv);
    assert top != null;
    top.setBackgroundColor(Color.parseColor(user[4]));
  }

  private void addOperation() {
    String [] op = new String[] {"编辑联系人", "分享联系人", "加入黑名单", "删除联系人"};
    ListView opList = (ListView) findViewById(R.id.operation);
    assert opList != null;
    opList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, op));
  }

  private void addClickListener() {
    final ImageView star = (ImageView) findViewById(R.id.star);
    ImageView back = (ImageView) findViewById(R.id.back);
    assert star != null;
    star.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (star.getTag(star.getId()) == Boolean.TRUE) {
          star.setTag(star.getId(), Boolean.FALSE);
          star.setImageResource(R.mipmap.empty_star);
        }
        else {
          star.setTag(star.getId(), Boolean.TRUE);
          star.setImageResource(R.mipmap.full_star);
        }
      }
    });

    assert back != null;
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

  }
}
