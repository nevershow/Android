package ml.huangjw.lab8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

/**
 * Created by Huangjw on 2016/11/18.
 */
public class MyAdapter extends BaseAdapter {
  private Context context;
  List<Vector<String>> data;

  public MyAdapter(Context context, List<Vector<String>> data) {
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
      viewHolder.name = (TextView) view.findViewById(R.id.nameTv);
      viewHolder.birth = (TextView) view.findViewById(R.id.birthTv);
      viewHolder.gift = (TextView) view.findViewById(R.id.giftTv);
      view.setTag(viewHolder);
    }
    else {
      view = convertView;
      viewHolder = (ViewHolder) view.getTag();
    }
    viewHolder.name.setText(data.get(position).get(0));
    String s = data.get(position).get(1) + "." + data.get(position).get(2);
    viewHolder.birth.setText(s);
    viewHolder.gift.setText(data.get(position).get(3));
    return view;
  }

  private class ViewHolder {
    public TextView name;
    public TextView birth;
    public TextView gift;
  }
}
