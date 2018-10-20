package com.example.thatnight.wanandroid.base;

/**
 * Created by thatnight on 2017.10.27.
 */

public abstract class BasePagerFragment<V extends BaseContract.IBaseView,
        P extends BasePresenter> extends BaseFragment<V, P> implements BaseContract.IBaseView {

    @Override
    protected void isLoadData() {
        isCanLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     */
    private void isCanLoadData() {
        if (!mIsPrepare || mIsLoad) {
            return;
        }
        if (getUserVisibleHint()) {
            onLazyLoad();
            mIsLoad = true;
        }
    }

    @Override
    protected void initToolBar() {

    }
}
