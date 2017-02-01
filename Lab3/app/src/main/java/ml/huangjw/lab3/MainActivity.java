package ml.huangjw.lab3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
  String [] name;
  AlertDialog.Builder isDelete;
  List<Map<String, String>> data;
//  SimpleAdapter simpleAdapter;
  MyAdapter myAdapter;
  int clickPos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
    addAdapter();
    addClickListener();
  }

  private void init() {
    Vector<String> nameV = new Vector<String>();
    InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.list));
    BufferedReader reader = new BufferedReader(inputStreamReader);
    try {
      String line = reader.readLine();
      while (line != null) {
        nameV.add(line.split(" ")[0]);
        line = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    name = (String[]) nameV.toArray(new String[10]);

    isDelete = new AlertDialog.Builder(MainActivity.this);
    isDelete.setTitle("删除联系人").setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {}
    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        data.remove(clickPos);
        myAdapter.notifyDataSetChanged();
      }
    });
  }

  private void addAdapter() {
    data = new ArrayList<>();
    ListView namelist = (ListView) findViewById(R.id.namelist);
    assert namelist != null;
    for (String s : name) {
      Map<String, String> temp = new HashMap<>();
      temp.put("firstLetter", s.substring(0, 1));
      temp.put("name", s);
      data.add(temp);
    }
//    simpleAdapter = new SimpleAdapter(this, data, R.layout.item, new String[] {"firstLetter", "name"}, new int[] {R.id.firstLetter, R.id.name});
    myAdapter = new MyAdapter(this, data);
    namelist.setAdapter(myAdapter);
  }

  private void addClickListener() {
    ListView namelist = (ListView) findViewById(R.id.namelist);
    assert namelist != null;
    namelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        startActivity(new Intent(MainActivity.this, Activity2.class).putExtra("name", data.get(position).get("name")));
      }
    });
    namelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        clickPos = position;
        isDelete.setMessage("确定删除联系人" + data.get(clickPos).get("name") + "？").show();
        return true;
      }
    });
  }
}
