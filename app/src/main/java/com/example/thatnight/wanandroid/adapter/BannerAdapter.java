package com.example.thatnight.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.example.thatnight.wanandroid.entity.BannerEntity;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * date: 2018/9/3
 * author: thatnight
 */
public class BannerAdapter extends PagerAdapter {

    private List<BannerEntity> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    private OnBannerClickListener mOnBannerClickListener;

    public BannerAdapter(Context context, List<BannerEntity> list, OnBannerClickListener onBannerClickListener) {
        mList = list;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_UNCHANGED;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.item_banner_photo, null);
        ImageView photoView = view.findViewById(R.id.item_photo);
        // TODO: 2017.5.22 变形问题解决: 不使用动画特效
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_pictures);
        String url = mList.get(position).getImagePath();
        Glide.with(mContext).load(url).apply(requestOptions).into(photoView);


        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBannerClickListener != null) {
                    mOnBannerClickListener.clickBanner(v, position);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public interface OnBannerClickListener {
        void clickBanner(View view, int position);
    }

    public void setData(List<BannerEntity> list) {
        if (list != null) {
            mList = list;
            notifyDataSetChanged();
        }
    }
}
