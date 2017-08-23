package com.hgz.test.jinritoutiao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hgz.test.jinritoutiao.R;
import com.hgz.test.jinritoutiao.adapter.MyListAdapter;
import com.hgz.test.jinritoutiao.bean.NewsInfo;
import com.hgz.test.jinritoutiao.dao.SQLiteDao2;
import com.limxing.xlistview.view.XListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MyFragment extends Fragment implements XListView.IXListViewListener {

    private View view;
    public  XListView lv;
    private boolean flag;
    private MyListAdapter myListAdapter;
    private SQLiteDao2 dao;
//    CallBackValue callBackValue;
    private List<NewsInfo.ResultBean.DataBean> data;
    private ArrayList<NewsInfo.ResultBean.DataBean> news;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myfragment, container, false);
        return view;
    }
    //定义一个回调接口
//    public interface CallBackValue{
//        public void SendMessageValue(XListView listView);
//    }
    //步骤2:设置监听器
//    public void setCallBack(CallBackValue callBackValue) {
//        this.callBackValue = callBackValue;
//    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dao = new SQLiteDao2(getContext());
        lv = (XListView) view.findViewById(R.id.lv);
        lv.setXListViewListener(this);
        lv.setPullLoadEnable(true);
        getDatas();
        news = dao.findNews();

        MyListAdapter myListAdapter = new MyListAdapter(getActivity(), news, lv);
        lv.setAdapter(myListAdapter);
    }

    private void getDatas() {
        String url = "http://v.juhe.cn/toutiao/index";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addBodyParameter("key", "844787efa15fd982318093a80655259f");
        requestParams.addBodyParameter("type", getArguments().getString("title"));
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                NewsInfo newsInfo = gson.fromJson(result, NewsInfo.class);
                data = newsInfo.getResult().getData();
                if (myListAdapter == null) {
                    myListAdapter = new MyListAdapter(getActivity(), data, lv);
                    lv.setAdapter(myListAdapter);
                } else {
                    lv.setAdapter(myListAdapter);
                    myListAdapter.loadMore(data, flag);
                    myListAdapter.notifyDataSetChanged();
                }
                if (news.size() == 0) {
                    for (NewsInfo.ResultBean.DataBean dataBean : data) {
                        dao.addNews(dataBean.getTitle(), dataBean.getAuthor_name(), dataBean.getDate());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onRefresh() {
        getDatas();
        flag = true;
        lv.stopRefresh(true);
    }

    @Override
    public void onLoadMore() {
        getDatas();
        flag = false;
        lv.stopLoadMore();
    }
//    MainActivity mainActivity;

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mainActivity= (MainActivity) context;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mainActivity=null;
//
//    }
//
//    public void sendDataToActivity(){
//       if(mainActivity!=null){
//           mainActivity.updataData(lv,data);
//       }
//    }
}
