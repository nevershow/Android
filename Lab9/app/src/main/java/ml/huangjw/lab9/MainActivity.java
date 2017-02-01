package ml.huangjw.lab9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
  private final int UPDATENOW = 0, UPDATESUGGESTION = 1, UPDATEDAILY = 2, UPDATEICON = 3, CITYNOTEXISTS = 4, NOTCHINA = 5;
  private final String HTTPURL = "http://apis.baidu.com/heweather/weather/free?city=";
  private final String ICONURL = "http://files.heweather.com/cond_icon/";
  private static Vector<String> suggestionList;
  private SuggestionAdapter myadapter;
  private long lastclick;

  private TextView city, nowtmp, tmprange, cond, humidity, wind, update;
  private ImageView icon;
  private RecyclerView weeklist;
  private Bitmap bitmap;
  private String citystr;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
  }

  private void init() {
    weeklist = (RecyclerView) findViewById(R.id.weeklist);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    weeklist.setLayoutManager(layoutManager);

    suggestionList = null;
    lastclick = System.currentTimeMillis();
    Button search = (Button) findViewById(R.id.search);
    final EditText cityname = (EditText) findViewById(R.id.cityname);
    search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        citystr = cityname.getText().toString().trim();
        if (citystr.isEmpty()) {
          showToast(R.string.emptycityname);
          return;
        }
        if (!hasNetwork()) {
          showToast(R.string.nonetwork);
          return;
        }
        long current = System.currentTimeMillis(), tmp = lastclick;
        lastclick = current;
        if (current - tmp < 600)
          showToast(R.string.toofast);
        else
          getWeather();
      }
    });

    city = (TextView) findViewById(R.id.city);
    nowtmp = (TextView) findViewById(R.id.nowtmp);
    tmprange = (TextView) findViewById(R.id.tmprange);
    cond = (TextView) findViewById(R.id.cond);
    humidity = (TextView) findViewById(R.id.humidity);
    wind = (TextView) findViewById(R.id.wind);
    update = (TextView) findViewById(R.id.update);
    icon = (ImageView) findViewById(R.id.icon);
  }

  private boolean hasNetwork() {
    ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }

  private void addAdapter() {
    suggestionList = new Vector<>(5);
    for (int i = 0; i < 5; ++i) suggestionList.add("");
    ListView listview = (ListView) findViewById(R.id.suggestionList);
    myadapter = new SuggestionAdapter(this, suggestionList);
    listview.setAdapter(myadapter);
    listview.setItemsCanFocus(false);
  }

  private void getWeather() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          URL url = new URL(HTTPURL + URLEncoder.encode(citystr, "utf-8"));
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestProperty("apikey", "48b13529ff54895d6837e64403a84b49");
          connection.setDoInput(true);
          if (connection.getResponseCode() == 200) {
            InputStream is =  connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line, json = "";
            while ((line = reader.readLine()) != null)
              json += line;
            is.close();
            connection.disconnect();
            decodeJson(json);
          }
          else
            showToast(R.string.timeout);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  };

  private void getIcon(final String code_d) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          bitmap = null;
          URL url = new URL(ICONURL + code_d + ".png");
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setDoInput(true);
          if (connection.getResponseCode() == 200) {
            InputStream is =  connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            connection.disconnect();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = UPDATEICON;
        handler.sendMessage(msg);
      }
    }).start();
  };

  private void decodeJson(String json) throws JSONException {
    JSONObject obj = new JSONObject(json);
    obj = obj.getJSONArray("HeWeather data service 3.0").getJSONObject(0);
    Message msg1 = new Message(), msg2 = new Message(), msg3 = new Message();
    if (obj.getString("status").equals("ok")) {
      msg1.what = UPDATESUGGESTION;
      if (!obj.has("suggestion")) { // 国外城市
        msg1.what = NOTCHINA;
        handler.sendMessage(msg1);
        return;
      }
      // 更新生活指数
      msg1.what = UPDATESUGGESTION;
      msg1.obj = obj.getJSONObject("suggestion");
      handler.sendMessage(msg1);

      // 更新今日天气信息
      JSONArray dailyforecast = obj.getJSONArray("daily_forecast");
      msg2.what = UPDATENOW;
      msg2.obj = dailyforecast.getJSONObject(0).put("update", obj.getJSONObject("basic").getJSONObject("update").getString("loc").substring(5)).put("nowtmp", obj.getJSONObject("now").getString("tmp"));
      handler.sendMessage(msg2);

      // 更新未来6天天气信息
      dailyforecast.remove(0);
      msg3.what = UPDATEDAILY;
      msg3.obj = dailyforecast;
      handler.sendMessage(msg3);
    } else if (obj.getString("status").equals("unknown city")) { //城市名不存在
      msg3.what = CITYNOTEXISTS;
      handler.sendMessage(msg3);
    }
  }

  Handler handler = new Handler() {
    public void handleMessage(Message msg) {
      try {
        switch (msg.what) {
          case NOTCHINA:
            showToast(R.string.notChina);
            break;
          case UPDATENOW:
            updateNow((JSONObject) msg.obj);
            break;
          case UPDATESUGGESTION:
            updateSuggestion((JSONObject) msg.obj);
            break;
          case UPDATEICON:
            icon.setImageBitmap(bitmap);
            break;
          case UPDATEDAILY:
            weeklist.setAdapter(new WeekAdapter(MainActivity.this, (JSONArray) msg.obj));
            break;
          case CITYNOTEXISTS:
            showToast(R.string.citynoexists);
          break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };

  private void updateSuggestion(JSONObject obj) throws JSONException {
    if (suggestionList == null)
      addAdapter();
    JSONObject suggestion;
    String [] keys = {"uv", "flu", "drsg", "cw", "sport"};
    for (int i = 0; i < 5; ++i) {
      suggestion = obj.getJSONObject(keys[i]);
      suggestionList.set(i, suggestion.getString("txt"));
    }
    myadapter.notifyDataSetChanged();
  }

  private void updateNow(JSONObject obj) throws Exception {
    city.setText(citystr);
    nowtmp.setText(obj.getString("nowtmp") + "°C");
    JSONObject tmp = obj.getJSONObject("tmp");
    tmprange.setText(tmp.getString("min") + "°C/" + tmp.getString("max") + "°C");
    cond.setText(obj.getJSONObject("cond").getString("txt_d"));
    humidity.setText("湿度: " + obj.getString("hum") + "%");
    wind.setText(obj.getJSONObject("wind").getString("dir") + " " + obj.getJSONObject("wind").getString("sc"));
    update.setText(obj.getString("update") + " 更新");
    getIcon(obj.getJSONObject("cond").getString("code_d"));
  }

  private void showToast(int str) {Toast.makeText(this, str, Toast.LENGTH_SHORT).show();}
}