package ml.huangjw.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Huangjw on 2016/10/16.
 */
public class MyAdapter extends BaseAdapter{
  private Context context;
  List<Map<String, String>> data;

  public MyAdapter(Context context, List<Map<String, String>> data) {
    this.context = context;
    this.data = data;
  }

  @Override
  public int getCount() {
    return data == null ? 0 : data.size();
  }

  @Override
  public Object getItem(int position) {
    return data == null ? null : data.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view;
    ViewHolder viewHolder;
    if (convertView == null) {
      view = LayoutInflater.from(context).inflate(R.layout.item, null);
      viewHolder = new ViewHolder();
      viewHolder.firstLetter = (TextView) view.findViewById(R.id.firstLetter);
      viewHolder.name = (TextView) view.findViewById(R.id.name);
      view.setTag(viewHolder);
    }
    else {
      view = convertView;
      viewHolder = (ViewHolder) view.getTag();
    }
    viewHolder.firstLetter.setText(data.get(position).get("firstLetter"));
    viewHolder.name.setText(data.get(position).get("name"));
    return view;
  }

  private class ViewHolder {
    public TextView firstLetter;
    public TextView name;
  }
}
