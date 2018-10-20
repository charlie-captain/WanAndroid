package com.example.thatnight.wanandroid.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.NewArticleRvAdapter;
import com.example.thatnight.wanandroid.adapter.SearchAdapter;
import com.example.thatnight.wanandroid.base.BaseDialogFragment;
import com.example.thatnight.wanandroid.base.BaseRecyclerViewAdapter;
import com.example.thatnight.wanandroid.callback.MvpBooleanCallback;
import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;
import com.example.thatnight.wanandroid.entity.HotKey;
import com.example.thatnight.wanandroid.entity.Msg;
import com.example.thatnight.wanandroid.mvp.contract.SearchContract;
import com.example.thatnight.wanandroid.mvp.model.BaseFuncModel;
import com.example.thatnight.wanandroid.mvp.presenter.SearchPresenter;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.HelperCallback;
import com.example.thatnight.wanandroid.utils.LoginContextUtil;
import com.example.thatnight.wanandroid.utils.OkHttpStringResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;
import com.example.thatnight.wanandroid.utils.SharePreferenceUtil;
import com.example.thatnight.wanandroid.utils.ToastUtil;
import com.example.thatnight.wanandroid.utils.UiHelper;
import com.example.thatnight.wanandroid.view.activity.ArticleWebViewActivity;
import com.example.thatnight.wanandroid.view.activity.SearchActivity;
import com.example.thatnight.wanandroid.view.customview.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SearchDialogFragment extends BaseDialogFragment implements SearchContract.IView, View.OnClickListener, OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnClickRecyclerViewListener, NewArticleRvAdapter.OnArticleItemClickListener {

    private ImageButton mBack;
    private List<Article> mArticles;
    private NewArticleRvAdapter mAdapter;
    private SearchAdapter mSearchAdatper;
    private RecyclerView mRv;
    private EditText mSearchView;
    private ImageButton mIbtnClear;
    private RefreshLayout mRefreshLayout;
    private int mPage = 1;
    private View mIbtnCollect;
    private int mSelectPosition;
    private List<String> mSearchHistory;
    private ItemTouchHelper.Callback mItemCallback;
    private boolean isEditting = false;
    private BaseFuncModel mBaseFuncModel;
    private String mUrl = Constant.URL_BASE + Constant.URL_WECHAT_LIST + "%s/%d/json";
    private String mKey;
    private String mSearchUrl;
    public static final String KEY_HISTORY = "search_wechat_list";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Override
    protected void isLoadData() {

    }

    public static SearchDialogFragment newIntent(String key) {
        SearchDialogFragment fragment = new SearchDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected SearchPresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_search;
    }

    @Override
    protected void initView() {
        mBack = (ImageButton) $(R.id.ibtn_back);
        mRefreshLayout = (RefreshLayout) $(R.id.srl_search);
        mRv = (RecyclerView) $(R.id.rv_search);
        mIbtnClear = (ImageButton) $(R.id.tb_search_clear);
        mAdapter = new NewArticleRvAdapter();

        mRv.setItemAnimator(new DefaultItemAnimator());
        mRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recyclerview_decoration)));
        mRv.setAdapter(mSearchAdatper);
        mItemCallback = new HelperCallback(mSearchAdatper);
        ItemTouchHelper touchHelper = new ItemTouchHelper(mItemCallback);
        touchHelper.attachToRecyclerView(mRv);

        mSearchView = (EditText) $(R.id.tb_searchview);

    }

    @Override
    protected void initData(Bundle arguments) {
        mKey = arguments.getString("key");
        mArticles = new ArrayList<>();
        String history = SharePreferenceUtil.getInstance().optString(KEY_HISTORY);
        if (!TextUtils.isEmpty(history)) {
            mSearchHistory = GsonUtil.gsonToList(history, String.class);
        }
        mSearchAdatper = new SearchAdapter();
        mSearchAdatper.updateData(mSearchHistory);
        mBaseFuncModel = new BaseFuncModel();
        if (!TextUtils.isEmpty(mKey)) {
            mSearchUrl = String.format(mUrl, mKey, mPage);
        }
    }

    @Override
    protected void initListener() {
        mBack.setOnClickListener(this);
        mSearchAdatper.setOnRecyclerViewListener(new BaseRecyclerViewAdapter.OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int pos) {
                mSearchView.setText(mSearchHistory.get(pos));
                mSearchView.setSelection(mSearchHistory.get(pos).length());
                addHistory(mSearchHistory.get(pos));
                UiHelper.inputSoftWare(false, mSearchView);
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
                        search(v.getText().toString().trim());
                        UiHelper.inputSoftWare(false, v);
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
                    mRv.setAdapter(mSearchAdatper);
                    isEditting = false;
                } else {
                    search(s.toString().trim());
                }
            }
        });
    }

    /**
     * 搜索
     *
     * @param s
     */
    public void search(String s) {
        String url = mSearchUrl + "?k=" + s;
        getArticle(true, url);
    }

    /**
     * 获取
     *
     * @param isRefresh
     * @param url
     */
    public void getArticle(final boolean isRefresh, String url) {
        mBaseFuncModel.getArticle(isRefresh, url, new MvpBooleanCallback() {
            @Override
            public void onResult(boolean b, Msg msg) {
                if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
                    if (isRefresh) {
                        refreshData((List<Article>) msg.getData());
                    } else {
                        loadmoreData((List<Article>) msg.getData());
                    }
                } else {
                    showToast(msg.getErrorMsg().toString());
                }
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
    protected void onLazyLoad() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_search_clear:
                if (!TextUtils.isEmpty(mSearchView.getText())) {
                    mSearchView.setText("");
                }
                break;
            case R.id.ibtn_back:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void showHotKey(List<HotKey> hotKeys) {

    }

    @Override
    public void error(String s) {
        mRefreshLayout.finishRefresh(false);
        mRefreshLayout.finishLoadmore(false);
    }

    @Override
    public void onCollect(boolean isSuccess, String s) {
        Snackbar.make(mRv, s, Snackbar.LENGTH_SHORT).setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIbtnCollect != null) {
                    UiHelper.setSelected(mIbtnCollect);
                }
            }
        }).show();
        if (!isSuccess) {
            if (mIbtnCollect != null) {
                UiHelper.setSelected(mIbtnCollect);
            }
        }
    }

    @Override
    public <T> void refreshData(List<T> datas) {
        mRefreshLayout.finishRefresh(true);
        mArticles = (List<Article>) datas;
        mAdapter.updateData(mArticles);
        if (!isEditting) {
            mRv.setAdapter(mAdapter);
            isEditting = true;
        }
    }

    @Override
    public <T> void loadmoreData(List<T> datas) {
        mRefreshLayout.finishLoadmore(true);
        mArticles.addAll((Collection<? extends Article>) datas);
        mAdapter.updateData(mArticles);
        if (!isEditting) {
            mRv.setAdapter(mAdapter);
            isEditting = true;
        }
    }

    @Override
    public void isLoading(boolean isLoading) {

    }

    @Override
    public void showToast(String s) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mSearchView != null && !TextUtils.isEmpty(mSearchView.getText().toString())) {
            mPage = 1;
            search(mSearchView.getText().toString());
        } else {
            mRefreshLayout.finishRefresh(false);
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if (mSearchView != null && !TextUtils.isEmpty(mSearchView.getText().toString())) {
            mPage += 1;
            String url = String.format(mUrl, mKey, mPage) + "?k=" + mSearchView.getText().toString();
            getArticle(false, url);
        } else {
            mRefreshLayout.finishLoadmore(false);
        }
    }

    @Override
    public void onItemClick(int pos) {
        Article article = mArticles.get(pos);
        Intent intent = ArticleWebViewActivity.newIntent(mActivity, article, pos);
        startActivityForResultAnim(intent, 1);
    }

    @Override
    public void onItemLongClick(int pos) {
        UiHelper.showCopyArticleDialog(mActivity, mArticles, pos);
    }

    @Override
    public void onIbtnClick(View v, int position) {
        if (LoginContextUtil.getInstance().getUserState().collect(mActivity)) {
            mIbtnCollect = v;
            mSelectPosition = position;
            UiHelper.setSelected(v);
            mBaseFuncModel.collect(v.isSelected(), mArticles.get(position).getId(), new MvpBooleanCallback() {
                @Override
                public void onResult(boolean b, Msg msg) {
                    if (msg == null) {
                        ToastUtil.showToast("收藏失败, 网络出现错误!");
                    } else {
                        if (b) {
                            ToastUtil.showToast("收藏成功");
                        } else {
                            ToastUtil.showToast("收藏失败");
                        }
                    }
                }
            });
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
        CommentBottomDialogFragment.newInstance(mArticles.get(position)).show(getChildFragmentManager(), "comment");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null && dialog.getWindow().getDecorView() != null) {
            dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSearchHistory != null) {
            if (mSearchAdatper != null) {
                mSearchHistory = mSearchAdatper.getData();
            }
            SharePreferenceUtil.getInstance().putString(KEY_HISTORY, GsonUtil.gsonToJson(mSearchHistory));
        }
    }
}
