package com.example.k.chat20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.k.chat20.R;
import com.example.k.chat20.datastruct.Contacter;

import java.util.ArrayList;

/*
 * * 新建一个类继承BaseAdapter，实现视图与数据的绑定
 */
public class AdapterContacterItem extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private ArrayList<Contacter> listItem;

    /*构造函数*/
    public AdapterContacterItem(Context context, ArrayList<Contacter> listItem) {
        this.mInflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*书中详细解释该方法*/
    @Override  /*绘制每一行的view*/
    public View getView(final int position, View convertView, ViewGroup parent) {

        ContacterViewHolder holder;

        //观察convertView随ListView滚动情况
        //Log.v("MyListViewBase", "getView " + position + " " + convertView);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contacter_item, null);
            holder = new ContacterViewHolder();

            /*得到各个控件的对象*/
            holder.ItemImage = convertView.findViewById(R.id.ItemImage);
            holder.ItemNick = convertView.findViewById(R.id.ItemNick);
            holder.ItemText = convertView.findViewById(R.id.ItemText);

            convertView.setTag(holder);//绑定ViewHolder对象
        }else{
            holder = (ContacterViewHolder) convertView.getTag();//取出ViewHolder对象
        }

        holder.ItemImage.setImageResource(R.mipmap.headimg);
        holder.ItemNick.setText( listItem.get(position).getContactName() );
        holder.ItemText.setText( "" );

        convertView.scrollTo(convertView.getScrollX(), convertView.getScrollY());
        return convertView;
    }

}

/*存放控件*/
final class ContacterViewHolder {
    ImageView ItemImage;
    TextView ItemNick;
    TextView ItemText;

}