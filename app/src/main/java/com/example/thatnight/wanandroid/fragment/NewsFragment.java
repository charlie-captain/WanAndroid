package com.example.thatnight.wanandroid.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewsRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.bean.NewArticle;
import com.example.thatnight.wanandroid.utils.OkHttpResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.view.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by thatnight on 2017.10.27.
 */

public class NewsFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, NewsRvAdapter.IOnIbtnClickListener {

    private List<NewArticle> mArticles;

    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private NewsRvAdapter mAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected void initData(Bundle arguments) {
        mArticles = new ArrayList<>();
    }

    @Override
    protected void initView() {
        mRv = $(R.id.rv_main);
        mRefreshLayout = $(R.id.srl_main);
        mAdapter = new NewsRvAdapter();
        mRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recyclerview_decoration)));
        mRv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mAdapter.setOnRecyclerViewListener(this);
        mAdapter.setOnIbtnClickListener(this);
    }

    @Override
    protected void onLazyLoad() {
        if (mArticles != null && mArticles.size() > 0) {
            return;
        }
        Log.d("onlazy", "onLazyLoad: ");
        OkHttpUtil.getInstance().getAsync("http://www.wanandroid.com", new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.d("jsoup", e.toString());
            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);

                Document document = Jsoup.parse(response);

                Elements list = document.select("ul.list_article");
                Document listdoc = Jsoup.parse(list.toString());

                Elements name = listdoc.getElementsByTag("li");

                for (Element e : name) {
                    Elements info_art = e.select("div.info_art");

                    String title = info_art.get(0).getElementsByTag("a").text();
                    String url = info_art.get(0).select("a").attr("href");

                    Elements info = info_art.get(0).select("span");
                    String author = info.get(0).text();
                    String type = info.get(1).getElementsByTag("a").text();
                    String typeUrl = info.get(1).select("a").attr("href");
                    String time = info.get(2).text();


                    Elements info_opt = e.select("div.info_opt");
                    String artid = info_opt.get(0).select("span").attr("artid");

                    NewArticle article = new NewArticle();
                    article.setArtId(artid);
                    article.setAuthor(author);
                    article.setName(title);
                    article.setTime(time);
                    article.setType(type);
                    article.setTypeUrl(typeUrl);
                    mArticles.add(article);
                    Log.d("jsoup", title + "  " + url + " " + author + " " + type + " " + typeUrl + " " + time + " " + artid);
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.updateData(mArticles);
                    }
                });
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }


    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemLongClick(int pos) {

    }


    @Override
    public void onClick(View v, int position) {
        if (v != null) {
            if (v.isSelected()) {
                v.setSelected(false);
            } else {
                v.setSelected(true);
            }
        }
    }
}
