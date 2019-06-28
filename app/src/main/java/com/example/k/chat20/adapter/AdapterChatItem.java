package com.example.k.chat20.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.k.chat20.R;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * * 新建一个类继承BaseAdapter，实现视图与数据的绑定
 */
public class AdapterChatItem extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private ArrayList<HashMap<String, Object>> listItem;

    /*构造函数*/
    public AdapterChatItem(Context context, ArrayList<HashMap<String, Object>> listItem) {
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

        ChatViewHolder holder;

        //观察convertView随ListView滚动情况
        //Log.v("MyListViewBase", "getView " + position + " " + convertView);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.chat_item, null);
            holder = new ChatViewHolder();

            /*得到各个控件的对象*/
            holder.leftLayout = convertView.findViewById(R.id.chatitem_left_layout);
            holder.rightLayout = convertView.findViewById(R.id.chatitem_right_layout);
            holder.leftNick = convertView.findViewById(R.id.chatitem_left_tv);
            holder.rightNick = convertView.findViewById(R.id.chatitem_right_tv);
            holder.rightMsg = convertView.findViewById(R.id.chatitem_right_msg);
            holder.leftMsg = convertView.findViewById(R.id.chatitem_left_msg);
            convertView.setTag(holder);//绑定ViewHolder对象
        }else{
            holder = (ChatViewHolder) convertView.getTag();//取出ViewHolder对象
        }

        System.out.println( "------------" + listItem.get(position).get("Content") );
        if( listItem.get(position).get("Sender").equals("me") ){
            holder.rightMsg.setText( listItem.get(position).get("Content").toString() );
            holder.rightNick.setText( listItem.get(position).get("Sender").toString() );

            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setVisibility(View.VISIBLE);
        }else{
            holder.leftMsg.setText( listItem.get(position).get("Content").toString() );
            holder.leftNick.setText( listItem.get(position).get("Sender").toString() );
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setVisibility(View.VISIBLE);
        }

        convertView.scrollTo(convertView.getScrollX(), convertView.getScrollY());
        return convertView;
    }





}

/*存放控件*/
final class ChatViewHolder {
    ConstraintLayout leftLayout;
    ConstraintLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        TextView leftNick;
        TextView rightNick;

}
