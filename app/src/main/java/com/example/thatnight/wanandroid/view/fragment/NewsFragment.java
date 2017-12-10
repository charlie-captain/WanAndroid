package com.example.thatnight.wanandroid.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewsRvAdapter;
import com.example.thatnight.wanandroid.base.BaseFragment;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.mvp.contract.NewsContract;
import com.example.thatnight.wanandroid.mvp.model.NewsModel;
import com.example.thatnight.wanandroid.mvp.presenter.NewsPresenter;
import com.example.thatnight.wanandroid.utils.ViewUtil;
import com.example.thatnight.wanandroid.view.activity.WebViewActivity;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thatnight on 2017.10.27.
 */

public class NewsFragment extends BaseFragment<NewsContract.IView, NewsPresenter>
        implements OnRefreshListener,
        OnLoadmoreListener,
        BaseRecyclerViewAdapter.OnClickRecyclerViewListener,
        NewsRvAdapter.IOnIbtnClickListener,
        View.OnClickListener, NewsContract.IView {

    private List<Article> mArticles;

    private RecyclerView mRv;
    private RefreshLayout mRefreshLayout;
    private NewsRvAdapter mAdapter;
    private int mPage;
    private View mIbtnCollect;
    private int mSelectPosition;
    private Handler mHandler = new Handler();

    @Override
    protected void initData(Bundle arguments) {
        mArticles = new ArrayList<>();
    }

    @Override
    protected void initView() {
        mRv = mRootView.findViewById(R.id.rv_main);
        mRefreshLayout = mRootView.findViewById(R.id.srl_main);
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
        mPage = 0;
    }

    @Override
    protected void onLazyLoad() {
        if (mArticles != null && mArticles.size() > 0) {
            return;
        }
        mPresenter.getArticle(true, mPage);
//        Log.d("onlazy", "onLazyLoad: ");
//        OkHttpUtil.getInstance().getAsync("http://www.wanandroid.com", new OkHttpResultCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//                Log.d("jsoup", e.toString());
//            }
//
//            @Override
//            public void onResponse(byte[] bytes) {
//                String response = new String(bytes);
//
//                Document document = Jsoup.parse(response);
//
//                Elements list = document.select("ul.list_article");
//                Document listdoc = Jsoup.parse(list.toString());
//
//                Elements name = listdoc.getElementsByTag("li");
//
//                for (Element e : name) {
//                    Elements info_art = e.select("div.info_art");
//
//                    String title = info_art.get(0).getElementsByTag("a").text();
//                    String url = info_art.get(0).select("a").attr("href");
//
//                    Elements info = info_art.get(0).select("span");
//                    String author = info.get(0).text();
//                    String type = info.get(1).getElementsByTag("a").text();
//                    String typeUrl = info.get(1).select("a").attr("href");
//                    String time = info.get(2).text();
//
//
//                    Elements info_opt = e.select("div.info_opt");
//                    String artid = info_opt.get(0).select("span").attr("artid");
//
//                    NewArticle article = new NewArticle();
//                    article.setArtId(artid);
//                    article.setAuthor(author);
//                    article.setName(title);
//                    article.setUrl(url);
//                    article.setTime(time);
//                    article.setType(type);
//                    article.setTypeUrl(typeUrl);
//                    mArticles.add(article);
//                    Log.d("jsoup", title + "  " + url + " " + author + " " + type + " " + typeUrl + " " + time + " " + artid);
//                }
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.updateData(mArticles);
//                    }
//                });
//            }
//        });
    }


    @Override
    protected BaseModel initModel() {
        return new NewsModel();
    }

    @Override
    protected NewsPresenter getPresenter() {
        return new NewsPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPresenter.getArticle(true, 0);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage += 1;
        mPresenter.getArticle(false, mPage);
    }


    @Override
    public void onItemClick(int pos) {
        Article article = mArticles.get(pos);
        Intent intent = WebViewActivity.newIntent(mActivity,
                article.getId(),
                article.getOriginId(),
                article.getTitle(),
                article.getLink(),
                article.isCollect());
        startActivityForresultAnim(intent, 1);
    }

    @Override
    public void onItemLongClick(int pos) {

    }


    @Override
    public void onIbtnClick(View v, int position) {
        mIbtnCollect = v;
        mSelectPosition = position;
        ViewUtil.setSelected(v);
        mPresenter.collect(v.isSelected(), String.valueOf(mArticles.get(position).getId()));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void isLoading(boolean isLoading) {
        if (isLoading) {
            mRefreshLayout.autoRefresh();
        } else {
            mRefreshLayout.finishRefresh();
        }
    }


    @Override
    public void refreshHtml(List<Article> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
        mAdapter.updateData(mArticles);
    }

    @Override
    public void loadMoreHtml(List<Article> articles) {
        mRefreshLayout.finishLoadmore();
        mArticles.addAll(articles);
        mAdapter.appendData(articles);
    }

    @Override
    public void isCollectSuccess(boolean isSuccess, String s) {
//        showToast(s);
        Snackbar.make(mRootView, s, Snackbar.LENGTH_SHORT)
                .setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIbtnCollect != null) {
                            ViewUtil.setSelected(mIbtnCollect);
                        }
                        mPresenter.collect(mIbtnCollect.isSelected(), String.valueOf(mArticles.get(mSelectPosition).getId()));
                    }
                }).show();
        if (!isSuccess) {
            if (mIbtnCollect != null) {
                ViewUtil.setSelected(mIbtnCollect);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                mPresenter.getArticle(true, 0);
//                mRefreshLayout.autoRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
