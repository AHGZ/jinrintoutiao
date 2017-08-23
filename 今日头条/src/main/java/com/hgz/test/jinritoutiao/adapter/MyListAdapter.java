package com.hgz.test.jinritoutiao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hgz.test.jinritoutiao.MyWebView;
import com.hgz.test.jinritoutiao.R;
import com.hgz.test.jinritoutiao.bean.NewsInfo;
import com.hgz.test.jinritoutiao.utils.GetImages;
import com.limxing.xlistview.view.XListView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MyListAdapter extends BaseAdapter {
    private Context context;
    private List<NewsInfo.ResultBean.DataBean> data;
    private LayoutInflater mLayoutInflater;
    private PopupWindow popupWindow;
    private TextView delete;
    private ImageView close;
    private XListView lv;
    public MyListAdapter(final Context context, final List<NewsInfo.ResultBean.DataBean> data, XListView lv) {
        this.context=context;
        this.data=data;
        mLayoutInflater = LayoutInflater.from(context);
        initPopupWindowView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,MyWebView.class);
                intent.putExtra("url",data.get(position-1).getUrl());
                context.startActivity(intent);
            }
        });
    }
    public void loadMore(List<NewsInfo.ResultBean.DataBean> datas, boolean flag) {
        for (NewsInfo.ResultBean.DataBean dataes : datas) {
            if (flag) {
                data.add(0,dataes);
            } else {
                data.add(dataes);
            }
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position%3==0){
            return 0;
        }else{
            return 1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder=null;
        ViewHolder2 holder2=null;
        switch (type){
            case 0:
                if (convertView==null){
                    convertView=convertView.inflate(context, R.layout.myfragment_listview_items1,null);
                    holder=new ViewHolder();
                    holder.image= (ImageView) convertView.findViewById(R.id.showIv1);
                    holder.text1= (TextView) convertView.findViewById(R.id.showTv1);
                    holder.text2= (TextView) convertView.findViewById(R.id.showTv2);
                    holder.text3= (TextView) convertView.findViewById(R.id.showTv3);
                    holder.imageLoad= (ImageView) convertView.findViewById(R.id.imageLoad1);
                    convertView.setTag(holder);
                }else{
                    holder= (ViewHolder) convertView.getTag();
                }
                break;
            case 1:
                if (convertView==null){
                    convertView=convertView.inflate(context, R.layout.myfragment_listview_items2,null);
                    holder2=new ViewHolder2();
                    holder2.image1= (ImageView) convertView.findViewById(R.id.showIv21);
                    holder2.image2= (ImageView) convertView.findViewById(R.id.showIv22);
                    holder2.image3= (ImageView) convertView.findViewById(R.id.showIv23);
                    holder2.text1= (TextView) convertView.findViewById(R.id.showTv21);
                    holder2.text2= (TextView) convertView.findViewById(R.id.showTv22);
                    holder2.text3= (TextView) convertView.findViewById(R.id.showTv23);
                    holder2.imageLoad= (ImageView) convertView.findViewById(R.id.imageLoad2);
                    convertView.setTag(holder2);
                }else{
                    holder2= (ViewHolder2) convertView.getTag();
                }
                break;
        }
        switch (type){
            case 0:
                holder.text1.setText(data.get(position).getTitle());
                holder.text2.setText(data.get(position).getAuthor_name());
                holder.text3.setText(data.get(position).getDate());
                GetImages.getImage(data.get(position).getThumbnail_pic_s(),holder.image);
                holder.imageLoad.setOnClickListener(new getPosition(position));
                break;
            case 1:
                holder2.text1.setText(data.get(position).getTitle());
                holder2.text2.setText(data.get(position).getAuthor_name());
                holder2.text3.setText(data.get(position).getDate());
                GetImages.getImage(data.get(position).getThumbnail_pic_s(),holder2.image1);
                GetImages.getImage(data.get(position).getThumbnail_pic_s02(),holder2.image2);
                GetImages.getImage(data.get(position).getThumbnail_pic_s03(),holder2.image3);
                holder2.imageLoad.setOnClickListener(new getPosition(position));
                break;
        }

        return convertView;
    }
    class ViewHolder{
        private ImageView image;
        private TextView text1;
        private TextView text2;
        private TextView text3;
        private ImageView imageLoad;
    }
    class ViewHolder2{
        private ImageView image1;
        private ImageView image2;
        private ImageView image3;
        private TextView text1;
        private TextView text2;
        private TextView text3;
        private ImageView imageLoad;
    }
    class getPosition implements View.OnClickListener {
        private int position;
        public getPosition(int positon){
            this.position=positon;
        }
        @Override
        public void onClick(View v) {
            int[] array = new int[2];
            v.getLocationOnScreen(array);
            int x = array[0];
            int y = array[1];
            showPopupWindow(v,position,x,y);
        }
    }
    private void initPopupWindowView(){
        View view = mLayoutInflater.inflate(R.layout.popupwindow, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        delete = (TextView) view.findViewById(R.id.delete_tv);
        close = (ImageView) view.findViewById(R.id.close_iv);
    }
    private void showPopupWindow(View parent,final int position, int x, int y){
        popupWindow.showAtLocation(parent,0,x,y);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
    }
}
