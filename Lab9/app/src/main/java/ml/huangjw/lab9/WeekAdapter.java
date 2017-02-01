package ml.huangjw.lab9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Huangjw on 2016/11/29.
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder>{
  private JSONArray dailyforcast;
  private LayoutInflater mInflater;

  public WeekAdapter(Context context, JSONArray items) {
    super();
    dailyforcast = items;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = mInflater.inflate(R.layout.week_item, viewGroup, false);
    ViewHolder holder = new ViewHolder(view);
    holder.Date = (TextView)view.findViewById(R.id.date);
    holder.Weather =(TextView)view.findViewById(R.id.weather);
    holder.Temp = (TextView)view.findViewById(R.id.temp);
    return holder;
  }
  @Override
  public void onBindViewHolder(final ViewHolder viewHolder, final int i){
    try {
      JSONObject obj = dailyforcast.getJSONObject(i);
      viewHolder.Date.setText(obj.getString("date").substring(5));
      viewHolder.Weather.setText(obj.getJSONObject("cond").getString("txt_d"));
      JSONObject tmp = obj.getJSONObject("tmp");
      String t = tmp.getString("min") + "°C/" + tmp.getString("max") + "°C";
      viewHolder.Temp.setText(t);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  @Override
  public int getItemCount() {return dailyforcast.length();}
  public static class ViewHolder extends RecyclerView.ViewHolder{
    public ViewHolder(View itemView) {
      super(itemView);
    }
    TextView Date;
    TextView Weather;
    TextView Temp;
  }
}