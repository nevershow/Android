package ml.huangjw.lab8;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

  private MyAdapter myAdapter;
  private List<Vector<String>> listItems;
  private int clickPos;
  private static final String DBNAME = "Lab8.db";
  private AlertDialog.Builder isDelete;
  public static MyDB mydb;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
    addAdapter();
  }

  private void init() {
    Button additem = (Button) findViewById(R.id.additem);
    additem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(MainActivity.this, AddItemActivity.class), 1);
      }
    });

    mydb = new MyDB(this, DBNAME, null, 1);

    isDelete = new AlertDialog.Builder(this);
    isDelete.setTitle("是否删除?").setNegativeButton("否", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {}
    }).setPositiveButton("是", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mydb.delete(listItems.get(clickPos).get(0));
        listItems.remove(clickPos);
        myAdapter.notifyDataSetChanged();
      }
    });
  }

  public void showMyDialog(final int position, String month, String day) {
    DatePickerDialog dialog = new DatePickerDialog(this, null, 2016, 11, 19);
    dialog.setTitle(R.string.dialogtitle);
    View dialogview = getLayoutInflater().inflate(R.layout.dialog, null);
    dialog.setView(dialogview);
    final DatePicker dp = (DatePicker) dialogview.findViewById(R.id.birthDate);
    dp.updateDate(2016, Integer.parseInt(month) - 1, Integer.parseInt(day));
    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);

    final String name = listItems.get(position).get(0);
    TextView nameTv = (TextView) dialogview.findViewById(R.id.nameTv), phoneTv = (TextView) dialogview.findViewById(R.id.phoneTv);
    nameTv.setText(name);
    final EditText giftEdit = (EditText) dialogview.findViewById(R.id.giftEdit);
    giftEdit.setText(listItems.get(position).get(3));

    Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, Phone.DISPLAY_NAME + "= ?", new String[]{name}, null);
    String phonenum = "无";
    if (cursor != null && cursor.moveToNext()) {
      phonenum = cursor.getString(0);
      cursor.close();
    }
    phoneTv.setText(phonenum);
    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消修改", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {}
    });
    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认修改", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        String gift = giftEdit.getText().toString();
        int month = dp.getMonth() + 1, day = dp.getDayOfMonth();
        mydb.update(name, month,day, gift);
        listItems.get(position).set(1, String.valueOf(month));
        listItems.get(position).set(2, String.valueOf(day));
        listItems.get(position).set(3, gift);
        myAdapter.notifyDataSetChanged();
      }
    });

    dialog.show();
  }

  private void addAdapter() {
    listItems = new ArrayList<>();
    Cursor cursor = mydb.getAll();
    while (cursor.moveToNext()) {
      String name = cursor.getString(0),
          birthMon = String.valueOf(cursor.getInt(1)),
          birthDay = String.valueOf(cursor.getInt(2)),
          gift = cursor.getString(3);
      Vector<String> v = new Vector<>(4);
      v.add(name);
      v.add(birthMon);
      v.add(birthDay);
      v.add(gift);
      listItems.add(v);
    }
    cursor.close();

    ListView listView = (ListView) findViewById(R.id.listview);
    myAdapter = new MyAdapter(this, listItems);
    listView.setAdapter(myAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showMyDialog(position, listItems.get(position).get(1), listItems.get(position).get(2));
      }
    });

    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        clickPos = position;
        isDelete.show();
        return true;
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      String name = data.getExtras().getString("name"),
          birthMon = data.getExtras().getString("birthMon"),
          birthDay = data.getExtras().getString("birthDay"),
          gift = data.getExtras().getString("gift");
      Vector<String> v = new Vector<>(4);
      v.add(name);
      v.add(birthMon);
      v.add(birthDay);
      v.add(gift);
      listItems.add(v);
      myAdapter.notifyDataSetChanged();
    }
  }
}
