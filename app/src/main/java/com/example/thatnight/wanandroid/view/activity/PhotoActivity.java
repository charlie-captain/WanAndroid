package com.example.thatnight.wanandroid.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.PhotoPagerAdapter;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.utils.FileUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends BaseActivity implements PhotoPagerAdapter.ImageOnLongClickListener, View.OnClickListener {
    ViewPager mVpDetailsPhoto;
    TextView mTvDetailsPhotoCount;
    private PhotoPagerAdapter mPagerAdapter;
    public static final String INTENT_URL = "img_url";
    private String mImgUrl;
    private int mIndex = 0;
    private int mSize = 0;
    private View mImageView;
    private static final String PHOTO_LIST = "photo_list";
    private static final String PHOTO_INDEX = "photo_index";
    private List<String> mPhotoList;
    private int mSelectedPosition = -1;

    private Dialog mPicDialog;

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
        intent.putExtra(INTENT_URL, imgUrl);
        return intent;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
        }
        mIndex = getIntent().getIntExtra(PHOTO_INDEX, 0);

        mPhotoList = (List<String>) getIntent().getExtras().getSerializable(PHOTO_LIST);
        if (mPhotoList == null) {
            mPhotoList = new ArrayList<String>();
            mImgUrl = getIntent().getStringExtra(INTENT_URL);
            mPhotoList.add(mImgUrl);
        }
        mSize = mPhotoList.size();
        mPagerAdapter = new PhotoPagerAdapter(this, mPhotoList, this);


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;

    }

    @Override
    protected Boolean isSetStatusBar() {
        return true;
    }


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        //        StatusBarUtil.setTransparentForImageViewInFragment(this, mImageView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    @Override
    public void longClick(View view, int position) {
        mImageView = view;
        mSelectedPosition = position;
        mPicDialog = new Dialog(this, R.style.DialogShareTheme);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_photo_longclick, null);
        //初始化视图
        root.findViewById(R.id.btn_dialog_save).setOnClickListener(this);
        root.findViewById(R.id.btn_dialog_share).setOnClickListener(this);
        root.findViewById(R.id.btn_dialog_cancel).setOnClickListener(this);
        mPicDialog.setContentView(root);

        //            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        ////            lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        ////            lp.height = root.getMeasuredHeight();
        ////            dialogWindow.setAttributes(lp);

        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        root.setLayoutParams(params);
        Window dialogWindow = mPicDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        mPicDialog.show();
    }

    @Override
    public void onClick(final View v) {
        if (R.id.btn_dialog_cancel != v.getId()) {
            AndPermission.with(this).permission(Permission.Group.STORAGE).rationale(null).onGranted(new Action() {
                @Override
                public void onAction(List<String> permissions) {
                    switch (v.getId()) {
                        case R.id.btn_dialog_share:
                            //由文件得到uri
                            FileUtil.sharePic(PhotoActivity.this, mPhotoList.get(mSelectedPosition));
                            break;
                        case R.id.btn_dialog_save:
                            //保存图片后发送广播通知更新数据库
                            FileUtil.savePic(PhotoActivity.this, mPhotoList.get(mSelectedPosition));
                            break;
                        default:
                            break;
                    }
                }
            }).start();
        }
        if (mPicDialog != null) {
            mPicDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
