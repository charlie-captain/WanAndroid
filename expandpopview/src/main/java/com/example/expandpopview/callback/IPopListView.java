package com.example.expandpopview.callback;

/**
 * Created by thatnight on 2017.11.29.
 */

public interface IPopListView {

    void setDrawable(int popViewTextSize, int popViewTextColor ,int popViewTextColorSelected);

    void setPopViewListener(OnPopViewListener listener);
}
