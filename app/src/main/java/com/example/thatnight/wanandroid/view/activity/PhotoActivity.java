package com.example.thatnight.wanandroid.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.PhotoPagerAdapter;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity<T> extends BaseActivity {
    ViewPager mVpDetailsPhoto;
    TextView mTvDetailsPhotoCount;
    private PhotoPagerAdapter mPagerAdapter;
    private List<T> mPhotoList;
    private String mImgUrl;
    private int mIndex = 0;
    private int mSize = 0;

    private static final String PHOTO_LIST = "photo_list";
    private static final String PHOTO_INDEX = "photo_index";


    public static <T> Intent newIntent(Context context, int index, ArrayList<T> list) {
        Intent intent = new Intent(context, PhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTO_LIST, list);
        intent.putExtras(bundle);
        intent.putExtra(PHOTO_INDEX, index);
        return intent;
    }


    public static <T> Intent newIntent(Context context, String imgUrl) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra("img_url", imgUrl);
        return intent;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        mIndex = getIntent().getIntExtra(PHOTO_INDEX, 0);

        mPhotoList = (List<T>) getIntent().getExtras().getSerializable(PHOTO_LIST);
        if (mPhotoList == null) {
            mPhotoList = (List<T>) new ArrayList<String>();
            mImgUrl = getIntent().getStringExtra("img_url");
            mPhotoList.add((T) mImgUrl);
        }
        mSize = mPhotoList.size();
        mPagerAdapter = new PhotoPagerAdapter(this, mPhotoList);


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;

    }

    @Override
    protected BaseModel initModel() {
        return null;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        mVpDetailsPhoto = findViewById(R.id.vp_photo);
        mTvDetailsPhotoCount = findViewById(R.id.tv_photo);
        mVpDetailsPhoto.setAdapter(mPagerAdapter);
        mTvDetailsPhotoCount.setText(mIndex + 1 + "/" + mSize);
        mVpDetailsPhoto.setCurrentItem(mIndex);
        mVpDetailsPhoto.setOffscreenPageLimit(2);
    }

    @Override
    protected void initListener() {
        mVpDetailsPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                mTvDetailsPhotoCount.setText(position + 1 + "/" + mSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void isLoading(boolean isLoading) {

    }
}
