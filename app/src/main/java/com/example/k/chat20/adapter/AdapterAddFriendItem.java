package com.example.k.chat20.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.R;

import java.util.ArrayList;
import java.util.HashMap;

/*
* * 新建一个类继承BaseAdapter，实现视图与数据的绑定
*/
public class AdapterAddFriendItem extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private ArrayList<HashMap<String, Object>> listItem;

    /*构造函数*/
    public AdapterAddFriendItem(Context context, ArrayList<HashMap<String, Object>> listItem) {
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

        ViewHolder holder;

        //观察convertView随ListView滚动情况
        Log.v("MyListViewBase", "getView " + position + " " + convertView);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.addfriend_item, null);
            holder = new ViewHolder();
            /*得到各个控件的对象*/
            holder.title = convertView.findViewById(R.id.addfriend_textView);
            holder.bt_refuse = convertView.findViewById(R.id.addfriend_btn_false);
            holder.bt_agree = convertView.findViewById(R.id.addfriend_btn_true);
            holder.tv_agree = convertView.findViewById(R.id.addfriend_tv_agree);
            holder.tv_refuse = convertView.findViewById(R.id.addfriend_tv_refuse);
            convertView.setTag(holder);//绑定ViewHolder对象
        }else{
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }

        /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
        holder.title.setText( listItem.get(position).get("Sender").toString());

        /*为Button添加点击事件*/
        holder.bt_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加好友失败
                    CHAT.serviceAddFriend.refuseAddFriend(
                            listItem.get(position).get("Sender").toString(),
                            (long)(listItem.get(position).get("MessageId")) );

                    holder.bt_agree.setVisibility(View.INVISIBLE);
                    holder.bt_refuse.setVisibility(View.INVISIBLE);
                    holder.tv_refuse.setVisibility(View.VISIBLE);
                }
        });


        /*为Button添加点击事件*/
        holder.bt_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加好友成功
                CHAT.serviceAddFriend.agreeAddFriend( listItem.get(position).get("Sender").toString(),
                        (long)(listItem.get(position).get("MessageId")) );

                holder.bt_agree.setVisibility(View.INVISIBLE);
                holder.bt_refuse.setVisibility(View.INVISIBLE);
                holder.tv_agree.setVisibility(View.VISIBLE);
            }

        });
        return convertView;
    }



}


/*存放控件*/
final class ViewHolder {
        public TextView title;
        public Button bt_refuse;
        public Button bt_agree;
        public TextView tv_agree;
        public TextView tv_refuse;

}
