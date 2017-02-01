package ml.huangjw.lab5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fruit extends BaseAdapter {
  private Context context;
  private Map<String, Integer> Pic;
  private List<String> data;

  public Fruit(Context context, List<String> data) {
    this.context = context;
    this.data = data;
    Pic = new HashMap<>();
    Pic.put("Apple", R.mipmap.apple);
    Pic.put("Banana", R.mipmap.banana);
    Pic.put("Cherry", R.mipmap.cherry);
    Pic.put("Coco", R.mipmap.coco);
    Pic.put("Kiwi", R.mipmap.kiwi);
    Pic.put("Orange", R.mipmap.orange);
    Pic.put("Pear", R.mipmap.pear);
    Pic.put("Strawberry", R.mipmap.strawberry);
    Pic.put("Watermelon", R.mipmap.watermelon);
  }

  @Override
  public int getCount() {return data == null ? 0 : data.size();}

  @Override
  public Object getItem(int position) {return data == null ? null : data.get(position);}

  @Override
  public long getItemId(int position) {return position;}

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view;
    ViewHolder viewHolder;
    if (convertView == null) {
      view = LayoutInflater.from(context).inflate(R.layout.item, null);
      viewHolder = new ViewHolder();
      viewHolder.fruitPic = (ImageView) view.findViewById(R.id.fruitpic);
      viewHolder.fruitName = (TextView) view.findViewById(R.id.fruitName);
      view.setTag(viewHolder);
    } else {
      view = convertView;
      viewHolder = (ViewHolder) view.getTag();
    }
    String name = data.get(position);
    viewHolder.fruitPic.setImageResource(Pic.get(name));
    viewHolder.fruitName.setText(name);
    return view;
  }

  private class ViewHolder {
    public ImageView fruitPic;
    public TextView fruitName;
  }
}
