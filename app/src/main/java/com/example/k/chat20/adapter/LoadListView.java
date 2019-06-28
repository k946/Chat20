package com.example.k.chat20.adapter;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class LoadListView extends ListView implements OnScrollListener {

    int firstVisibleItem; // 当前第一个可见的item的位置；
    View footer;
    int totalItemCount;
    int lastVisibleItem;
    boolean isLoading;
    ILoadListener2 iListener2;


    public LoadListView(Context context) {
        super(context);
        initView(context);
    }
    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public LoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer, null);
        //footer.findViewById(R.id.load_layout).setVisibility(GONE);
        //this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }


    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollStates) {


        if( firstVisibleItem == 0 && scrollStates == SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading = true;
                //footer.findViewById(R.id.load_layout).setVisibility(VISIBLE);
                //加载更多数据

                if (iListener2!=null) {
                    iListener2.onLoad();
                }
                //k();
            }
        }

    }

    public void k(){
        System.out.println("---------------上划加载::");

    }

    public void loadComplete(){
        isLoading = false;
        //footer.findViewById(R.id.load_layout).setVisibility(GONE);
    }

    public void setInterface(ILoadListener2 iListener2){
        this.iListener2 = iListener2;
    }

    public interface ILoadListener2{
        public void onLoad();
    }

}