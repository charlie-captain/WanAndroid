package com.example.thatnight.wanandroid.base;

import java.util.List;

/**
 * Created by thatnight on 2017.11.1.
 */

public class BaseContract {
    public interface IBaseModel {
    }

    public interface IBasePresenter {


    }

    public interface IBaseView {

        void isLoading(boolean isLoading);

        void showToast(String s);


    }

}
