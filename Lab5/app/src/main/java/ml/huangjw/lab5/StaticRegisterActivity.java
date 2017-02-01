package ml.huangjw.lab5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class StaticRegisterActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.static_layout);
    init();
  }

  private void init() {
    String [] arr = new String[] {"Apple", "Banana", "Cherry", "Coco", "Kiwi", "Orange", "Pear", "Strawberry", "Watermelon"};
    final List<String> data = Arrays.asList(arr);
    Fruit adapter = new Fruit(this, data);
    ListView fruitlist = (ListView) findViewById(R.id.frutilist);
    assert fruitlist != null;
    fruitlist.setAdapter(adapter);
    fruitlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sendBroadcast(new Intent("ml.huangjw.lab5.Widget").putExtra("fruit", data.get(position)));
      }
    });
  }
}
