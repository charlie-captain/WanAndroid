package com.example.thatnight.wanandroid.data;

import android.content.Context;
import android.telecom.Call;

import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.BannerEntity;
import com.example.thatnight.wanandroid.utils.GsonUtil;
import com.example.thatnight.wanandroid.utils.OkHttpStringResultCallback;
import com.example.thatnight.wanandroid.utils.OkHttpUtil;

import java.util.List;


/**
 * date: 2018/9/3
 * author: thatnight
 */
public class MainDataController extends BaseDataController {

    MainDataListener mListener;

    public MainDataController(Context context, MainDataListener mainDataListener) {
        mListener = mainDataListener;
        initData();
    }


    @Override
    protected void getData() {
        OkHttpUtil.getInstance().getAsync(Constant.URL_BASE + Constant.URL_BANNER, new OkHttpStringResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String data) {
                List<BannerEntity> bannerEntities = GsonUtil.gsonToList(data, BannerEntity.class);
                if (bannerEntities != null) {
                    if (mListener != null) {
                        mListener.updateBanner(bannerEntities);
                    }
                }
            }

        });
    }

    public interface MainDataListener {
        void updateBanner(List<BannerEntity> entities);
    }
}
