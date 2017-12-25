package com.example.expandpopview.callback;

import com.example.expandpopview.entity.KeyValue;

/**
 * Created by thatnight on 2017.11.27.
 */

public interface OnTwoListCallback {
    void returnParentKeyValue(int pos, KeyValue keyValue);

    void returnChildKeyValue(int pos, KeyValue keyValue);
}
