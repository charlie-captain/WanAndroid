package com.example.thatnight.wanandroid.view.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewArticleRvAdapter;
import com.example.thatnight.wanandroid.adapter.SearchAdapter;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.HotKey;
import com.example.thatnight.wanandroid.mvp.contract.SearchContract;
import com.example.thatnight.wanandroid.mvp.model.SearchModel;
import com.example.thatnight.wanandroid.mvp.presenter.SearchPresenter;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.HelperCallback;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.UiUtil;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity<SearchContract.IView, SearchPresenter> implements SearchContract.IView, View.OnClickListener, OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, NewArticleRvAdapter.OnArticleItemClickListener {

    private List<Article> mArticles;
    private NewArticleRvAdapter mAdapter;
    private SearchAdapter mSearchAdatper;
    private RecyclerView mRv;
    private EditText mSearchView;
    private ImageButton mIbtnClear;
    private RefreshLayout mRefreshLayout;
    private int mPage = 0;
    private View mIbtnCollect;
    private int mSelectPosition;
    private List<String> mSearchHistory;
    private ItemTouchHelper.Callback mItemCallback;
    private boolean isEditting = false;
    private String mKey;
    private TagFlowLayout mTagFlowLayout;
    private List<HotKey> mHotKeys;

    @Override
    protected Boolean isSetStatusBar() {
        return true;
    }

    @Override
    protected BaseModel initModel() {
        return new SearchModel();
    }

    @Override
    protected SearchPresenter getPresenter() {
        return new SearchPresenter();
    }

    public static Intent newIntent(Context context, String key) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("key", key);
        return intent;
    }

    @Override
    protected void initData() {
        mKey = getIntent().getStringExtra("key");
        mArticles = new ArrayList<>();
        String history = (String) SharePreferenceUtil.getInstance().getString("search_list", "");
        if (!TextUtils.isEmpty(history)) {
            mSearchHistory = GsonUtil.gsonToList(history, String.class);
        }
        mSearchAdatper = new SearchAdapter();
        mSearchAdatper.updateData(mSearchHistory);
    }

    @Override
    protected void initView() {
        mShowBack = true;
        mRefreshLayout = $(R.id.srl_search);
        mRv = $(R.id.rv_search);
        mIbtnClear = $(R.id.tb_search_clear);
        mAdapter = new NewArticleRvAdapter();
        mTagFlowLayout = $(R.id.layout_tagflow);

        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recyclerview_decoration)));
        mRv.setAdapter(mSearchAdatper);
        mItemCallback = new HelperCallback(mSearchAdatper);
        ItemTouchHelper touchHelper = new ItemTouchHelper(mItemCallback);
        touchHelper.attachToRecyclerView(mRv);

        mSearchView = $(R.id.tb_searchview);
        //外部调用
        if (!TextUtils.isEmpty(mKey)) {
            mSearchView.setText(mKey);
            mSearchView.setSelection(mKey.length());
            mPresenter.search(false, mKey, String.valueOf(mPage));
            addHistory(mKey);
        } else {
            mPresenter.getHotKey();
        }
    }

    @Override
    protected void initListener() {
        mSearchAdatper.setOnRecyclerViewListener(new BaseRecyclerViewAdapter.OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int pos) {
                mSearchView.setText(mSearchHistory.get(pos));
                mSearchView.setSelection(mSearchHistory.get(pos).length());
            }

            @Override
            public void onItemLongClick(int pos) {

            }
        });
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mAdapter.setOnRecyclerViewListener(this);
        mAdapter.setOnIbtnClickListener(this);
        mIbtnClear.setOnClickListener(this);
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v != null && v.getText() != null) {
                        mPresenter.search(true, v.getText().toString(), String.valueOf(mPage));
                        UiUtil.inputSoftWare(false, v);
                        addHistory(v.getText().toString().trim());
                        return true;
                    }
                }
                return false;
            }
        });

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    if (mHotKeys != null && mHotKeys.size() > 0) {
                        mTagFlowLayout.setVisibility(View.VISIBLE);
                    } else {
                        mPresenter.getHotKey();
                    }
                    mRv.setAdapter(mSearchAdatper);
                    isEditting = false;
                } else {
                    if (mTagFlowLayout != null && mTagFlowLayout.getVisibility() == View.VISIBLE) {
                        mTagFlowLayout.setVisibility(View.GONE);
                    }
                    mPresenter.search(true, s.toString(), String.valueOf(mPage));
                }
            }
        });
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mPage = 0;
                mSearchView.setText(mHotKeys.get(position).getName());
                mSearchView.setSelection(mHotKeys.get(position).getName().length());
                mTagFlowLayout.setVisibility(View.GONE);
                //添加到历史
                addHistory(mHotKeys.get(position).getName());
                return false;
            }
        });
    }

    /**
     * 添加到历史搜索
     *
     * @param s
     */
    private void addHistory(String s) {
        if (mSearchHistory == null) {
            mSearchHistory = new ArrayList<>();
        }
        if (mSearchHistory.contains(s)) {
            mSearchHistory.remove(s);
        }
        mSearchHistory.add(s);
        //反转数组
        Collections.reverse(mSearchHistory);
        mSearchAdatper.updateData(mSearchHistory);
    }

    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void showArticles(boolean isRefresh, List<Article> articles) {
        if (isRefresh) {
            mRefreshLayout.finishRefresh(true);
            mArticles.clear();
        } else {
            mRefreshLayout.finishLoadmore(true);
        }
        mArticles.addAll(articles);
        mAdapter.updateData(mArticles);
        if (!isEditting) {
            mRv.setAdapter(mAdapter);
            isEditting = true;
        }
    }

    @Override
    public void isCollectSuccess(boolean isSuccess, String s) {
        Snackbar.make(mRv, s, Snackbar.LENGTH_SHORT).setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIbtnCollect != null) {
                    UiUtil.setSelected(mIbtnCollect);
                }
                mPresenter.collect(mIbtnCollect.isSelected(), String.valueOf(mArticles.get(mSelectPosition).getId()));
            }
        }).show();
        if (!isSuccess) {
            if (mIbtnCollect != null) {
                UiUtil.setSelected(mIbtnCollect);
            }
        }
    }

    @Override
    public void showHotKey(List<HotKey> hotKeys) {
        mTagFlowLayout.setVisibility(View.VISIBLE);
        mHotKeys = hotKeys;
        mTagFlowLayout.setAdapter(new TagAdapter<HotKey>(hotKeys) {

            @Override
            public View getView(FlowLayout parent, int position, HotKey hotKey) {
                TextView textView = (TextView) View.inflate(SearchActivity.this, R.layout.layout_flow_tv, null);
                textView.setText(hotKey.getName());
                return textView;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
            }

            @Override
            public boolean setSelected(int position, HotKey hotKey) {
                return super.setSelected(position, hotKey);
            }
        });
    }

    @Override
    public void error(String s) {
        mRefreshLayout.finishRefresh(false);
        mRefreshLayout.finishLoadmore(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_search_clear:
                if (!TextUtils.isEmpty(mSearchView.getText())) {
                    mSearchView.setText("");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mSearchView != null && !TextUtils.isEmpty(mSearchView.getText().toString())) {
            mPage = 0;
            mPresenter.search(true, mSearchView.getText().toString(), String.valueOf(mPage));
        } else {
            mRefreshLayout.finishRefresh(false);
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (mSearchView != null && !TextUtils.isEmpty(mSearchView.getText().toString())) {
            mPage += 1;
            mPresenter.search(false, mSearchView.getText().toString(), String.valueOf(mPage));
        } else {
            mRefreshLayout.finishLoadmore(false);
        }
    }

    @Override
    public void onItemClick(int pos) {
        Article article = mArticles.get(pos);
        Intent intent = WebViewActivity.newIntent(this, article.getId(), article.getOriginId(), article.getTitle(), article.getLink(), article.isCollect());
        startActivityForResultAnim(intent, 1);
    }

    @Override
    public void onItemLongClick(int pos) {

    }

    @Override
    public void onIbtnClick(View v, int position) {
        if (LoginContextUtil.getInstance().getUserState().collect(this)) {
            mIbtnCollect = v;
            mSelectPosition = position;
            UiUtil.setSelected(v);
            mPresenter.collect(v.isSelected(), String.valueOf(mArticles.get(position).getId()));
        }
    }

    @Override
    public void onTypeClick(View v, int position) {

    }

    @Override
    public void onAuthorClick(View v, int position) {

    }

    @Override
    public void onCommentClick(View v, int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (mSearchView != null && TextUtils.isEmpty(mSearchView.getText().toString())) {
                    mPresenter.search(true, mSearchView.getText().toString(), String.valueOf(mPage));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchHistory != null) {
            if (mSearchAdatper != null) {
                mSearchHistory = mSearchAdatper.getData();
            }
            SharePreferenceUtil.getInstance().getString("search_list", GsonUtil.gsonToJson(mSearchHistory));
        }
        //隐藏输入法
        UiUtil.inputSoftWare(false, mSearchView);
    }
}
