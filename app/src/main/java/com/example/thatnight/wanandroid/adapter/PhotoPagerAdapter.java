package com.example.thatnight.wanandroid.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.base.BaseActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * Created by ThatNight on 2017.5.8.
 */

public class PhotoPagerAdapter<T> extends PagerAdapter {
    private List<T> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    private ImageOnLongClickListener mOnLongClickListener;

    public PhotoPagerAdapter(Context context, List<T> list, ImageOnLongClickListener onLongClickListener) {
        mList = list;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mOnLongClickListener = onLongClickListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.viewpager_details_photo, null);
        PhotoView photoView = view.findViewById(R.id.pv_details_photo);
        // TODO: 2017.5.22 变形问题解决: 不使用动画特效
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_pictures);
        Glide.with(mContext).load(mList.get(position)).apply(requestOptions).into(photoView);
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickListener != null) {
                    mOnLongClickListener.longClick(v, position);
                }
                return true;
            }
        });
//        photoView.setOnTapListener(new DragPhotoView.OnTapListener() {
//            @Override
//            public void onTap(DragPhotoView dragPhotoView) {
//                ((BaseActivity) mContext).finish();
//            }
//        });
//        photoView.setOnExitListener(new DragPhotoView.OnExitListener() {
//            @Override
//            public void onExit(DragPhotoView dragPhotoView, float v, float v1, float v2, float v3) {
//                ((BaseActivity) mContext).finish();
//            }
//        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).finish();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnLongClickListener(ImageOnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    public interface ImageOnLongClickListener {
        void longClick(View view, int position);
    }

}
