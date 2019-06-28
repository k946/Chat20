package com.example.k.chat20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.k.chat20.R;
import com.example.k.chat20.datastruct.RecentContact;

import java.util.ArrayList;

/*
 * * 新建一个类继承BaseAdapter，实现视图与数据的绑定
 */
public class AdapterMessageList extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private ArrayList<RecentContact> listItem;

    /*构造函数*/
    public AdapterMessageList(Context context, ArrayList<RecentContact> listItem) {
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

        MessageItemViewHolder holder;

        //观察convertView随ListView滚动情况
        //Log.v("MyListViewBase", "getView " + position + " " + convertView);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.messagelist_item,  null);
            holder = new MessageItemViewHolder();

            /*得到各个控件的对象*/
            holder.messagelist_img = convertView.findViewById(R.id.messagelist_img);
            holder.messagelist_tv_name = convertView.findViewById(R.id.messagelist_tv_name);
            holder.messagelist_tv_number = convertView.findViewById(R.id.messagelist_tv_number);
            holder.messagelist_tv_text = convertView.findViewById(R.id.messagelist_tv_text);

            convertView.setTag(holder);//绑定ViewHolder对象
        }else{
            holder = (MessageItemViewHolder) convertView.getTag();//取出ViewHolder对象
        }

        holder.messagelist_img.setImageResource(R.mipmap.headimg);
        holder.messagelist_tv_number.setText( listItem.get(position).getUnreadCount() );
        holder.messagelist_tv_text.setText(listItem.get(position).getText());
        holder.messagelist_tv_name.setText(listItem.get(position).getSender());

        convertView.scrollTo(convertView.getScrollX(), convertView.getScrollY());
        return convertView;
    }

}

/*存放控件*/
final class MessageItemViewHolder {
    ImageView messagelist_img;
    TextView messagelist_tv_name;
    TextView messagelist_tv_text;
    TextView messagelist_tv_number;

}