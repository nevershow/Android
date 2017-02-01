package ml.huangjw.lab9;

/**
 * Created by Huangjw on 2016/11/29.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

public class SuggestionAdapter extends BaseAdapter {
  private Context context;
  Vector<String> data;
  private String suggestions[] = {"紫外线指数", "感冒指数", "穿衣指数", "洗车指数", "运动指数"};
  public SuggestionAdapter(Context context, Vector<String> data) {
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
      view = LayoutInflater.from(context).inflate(R.layout.suggestion_item, null);
      viewHolder = new ViewHolder();
      viewHolder.suggestion = (TextView) view.findViewById(R.id.suggestion);
      viewHolder.detail = (TextView) view.findViewById(R.id.detail);
      view.setTag(viewHolder);
    }
    else {
      view = convertView;
      viewHolder = (ViewHolder) view.getTag();
    }
    viewHolder.suggestion.setText(suggestions[position]);
    viewHolder.detail.setText(data.get(position));
    return view;
  }

  private class ViewHolder {
    public TextView suggestion;
    public TextView detail;
  }
}