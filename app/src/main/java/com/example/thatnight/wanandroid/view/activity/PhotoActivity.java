package com.example.thatnight.wanandroid.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.adapter.PhotoPagerAdapter;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.base.BaseModel;
import com.example.thatnight.wanandroid.base.BasePresenter;
import com.example.thatnight.wanandroid.utils.ImageUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity<T> extends BaseActivity implements PhotoPagerAdapter.ImageOnLongClickListener, View.OnClickListener {
    ViewPager mVpDetailsPhoto;
    TextView mTvDetailsPhotoCount;
    private PhotoPagerAdapter mPagerAdapter;
    private List<T> mPhotoList;
    private String mImgUrl;
    private int mIndex = 0;
    private int mSize = 0;
    private View mImageView;
    private static final String PHOTO_LIST = "photo_list";
    private static final String PHOTO_INDEX = "photo_index";

    private Dialog mLongClickDialog;

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
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mIndex = getIntent().getIntExtra(PHOTO_INDEX, 0);

        mPhotoList = (List<T>) getIntent().getExtras().getSerializable(PHOTO_LIST);
        if (mPhotoList == null) {
            mPhotoList = (List<T>) new ArrayList<String>();
            mImgUrl = getIntent().getStringExtra("img_url");
            mPhotoList.add((T) mImgUrl);
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

    @Override
    public void longClick(View view, int position) {
        mImageView = view;
        if (mLongClickDialog == null) {
            mLongClickDialog = new Dialog(this, R.style.DialogShareTheme);
            LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                    R.layout.dialog_photo_longclick, null);
            //初始化视图
            root.findViewById(R.id.btn_dialog_save).setOnClickListener(this);
            root.findViewById(R.id.btn_dialog_share).setOnClickListener(this);
            root.findViewById(R.id.btn_dialog_cancel).setOnClickListener(this);
            mLongClickDialog.setContentView(root);
            Window dialogWindow = mLongClickDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
            root.measure(0, 0);
            lp.height = root.getMeasuredHeight();

            lp.alpha = 9f; // 透明度
            dialogWindow.setAttributes(lp);
        }

        if (mLongClickDialog.isShowing()) {
            mLongClickDialog.dismiss();
        }
        mLongClickDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_dialog_cancel != v.getId()) {
            AndPermission.with(this)
                    .requestCode(100)
                    .permission(Permission.STORAGE)
                    .callback(mPermissionListener)
                    .start();
        }
        switch (v.getId()) {
            case R.id.btn_dialog_share:
                //由文件得到uri
                if (ImageUtil.sharePhoto(mImageView) != null) {
                    Uri imageUri = Uri.fromFile(ImageUtil.sharePhoto(mImageView));
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "分享图片"));
                }
                break;
            case R.id.btn_dialog_save:
                //保存图片后发送广播通知更新数据库
                if (ImageUtil.saveFile(mImageView) != null) {
                    Uri uri = Uri.fromFile(ImageUtil.saveFile(mImageView));
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    showToast("图片保存成功");
                }
                break;
            case R.id.btn_dialog_cancel:
                if (mLongClickDialog.isShowing()) {
                    mLongClickDialog.dismiss();
                }
                break;
            default:
                break;
        }
        if (mLongClickDialog.isShowing()) {
            mLongClickDialog.dismiss();
        }
    }

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {

        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLongClickDialog != null) {
            mLongClickDialog.dismiss();
        }
    }
}
