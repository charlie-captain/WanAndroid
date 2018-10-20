package com.example.thatnight.wanandroid.view.customview;


import android.content.Context;
import android.net.Uri;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.view.activity.PhotoActivity;

public class IconPreference extends Preference {

    private View.OnClickListener mOnClickListener;
    private Uri mUri;

    private String iconUrl;
    private ViewHolder mViewHolder;

    public IconPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.layout_settings_user);
    }

    public IconPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconPreference(Context context) {
        this(context, null);
    }



//
//    @Override
//    protected void onBindView(View view) {
//        super.onBindView(view);

//        mImageView.setImageURI();
//        mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnClickListener != null) {
//                    mOnClickListener.onClick(v);
//                }
//            }
//        });
//    }


    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mViewHolder = new ViewHolder(holder.itemView);
        mViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    mOnClickListener.onClick(v);
                }else{
                    getContext().startActivity(PhotoActivity.newIntent(getContext(),iconUrl));
                }
            }
        });
        if (mUri != null) {
            mViewHolder.mImageView.setImageURI(mUri);
        } else if (!TextUtils.isEmpty(iconUrl)) {
            Glide.with(mViewHolder.mImageView.getContext()).load(iconUrl).into(mViewHolder.mImageView);
        }
    }

    class ViewHolder {
        ImageView mImageView;

        public ViewHolder(View view) {
            mImageView = view.findViewById(R.id.iv_settings_user_icon);
        }

        public void setImageView(String url) {
            Glide.with(mImageView.getContext()).load(url).into(mImageView);
        }

        public void setImageView(Uri uri) {
            Glide.with(mImageView.getContext()).load(uri).into(mImageView);
        }

        public ImageView getImageView() {
            return mImageView;
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    public void setIconUrl(String url) {
        iconUrl = url;
        if (mViewHolder != null) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_round);
            Glide.with(mViewHolder.mImageView.getContext()).load(url).apply(requestOptions).into(mViewHolder.mImageView);
        }
        notifyChanged();
    }

    public void setIconUrl(Uri url) {
        mUri = url;
        if (mViewHolder != null) {
            Glide.with(mViewHolder.mImageView.getContext()).load(url).into(mViewHolder.mImageView);
        }
        notifyChanged();
    }
}
