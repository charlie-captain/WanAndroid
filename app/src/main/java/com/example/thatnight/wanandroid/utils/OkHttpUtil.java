package com.example.thatnight.wanandroid.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Msg;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.okhttp3.Call;
import cn.bmob.v3.okhttp3.Callback;
import cn.bmob.v3.okhttp3.FormBody;
import cn.bmob.v3.okhttp3.Headers;
import cn.bmob.v3.okhttp3.MediaType;
import cn.bmob.v3.okhttp3.MultipartBody;
import cn.bmob.v3.okhttp3.OkHttpClient;
import cn.bmob.v3.okhttp3.Request;
import cn.bmob.v3.okhttp3.RequestBody;
import cn.bmob.v3.okhttp3.Response;


/**
 * http请求工具类, 封装了OkHttp3
 */

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static OkHttpUtil sOkHttpUtil;
    private OkHttpClient.Builder mOkHttpClientBuilder;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private static Context mContext;

    public static final int CONNECT_TIMEOUT = 6;
    public static final int READ_TIMEOUT = 8;
    public static final int WRITE_TIMEOUT = 8;

    private OkHttpUtil() {
        mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new OkHttpCookieJar(mContext)).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient = mOkHttpClientBuilder.build();
        mHandler = new Handler(Looper.getMainLooper());

    }

    public static void init(Context applicationContext) {
        mContext = applicationContext;
    }

    public static OkHttpUtil getInstance() {
        if (sOkHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                if (sOkHttpUtil == null) {
                    sOkHttpUtil = new OkHttpUtil();
                }
            }
        }
        return sOkHttpUtil;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 异步的Get请求
     */
    public void getAsync(String url, OkHttpResultCallback okHttpResultCallback) {
        Request request = new Request.Builder().url(url).build();
        deliveryResult(okHttpResultCallback, request);
    }

    /**
     * 异步的Get请求
     */
    public void getAsync(String url, final OkHttpStringResultCallback okHttpResultCallback) {
        Request request = new Request.Builder().url(url).build();
        deliveryResult(new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(byte[] bytes) {
                String response = new String(bytes);
                Msg msg = GsonUtil.gsonToBean(response, Msg.class);
                if (msg != null) {
                    if (msg.getErrorCode() == Constant.CODE_SUCCESS) {
                        if (msg.getData() != null) {
                            String data = GsonUtil.gsonToJson(msg.getData());
                            okHttpResultCallback.onResponse(data);
                        }
                    } else {
                        if (msg.getErrorMsg() != null) {
                            ToastUtil.showToast(msg.getErrorMsg().toString());
                        } else {
                            ToastUtil.showToast("服务器数据出错!");
                        }
                    }
                } else {
                    ToastUtil.showToast("服务器出现问题啦!");
                }
            }
        }, request);
    }

    /**
     * 异步带请求头的get请求
     *
     * @param url
     * @param okHttpResultCallback
     * @param headers
     */
    public void getAsync(String url, Map<String, String> headers, OkHttpResultCallback okHttpResultCallback) {
        Request request = buildGetRequest(url, headers);
        deliveryResult(okHttpResultCallback, request);
    }

    /**
     * 异步的post请求
     */
    public void postAsync(String url, Map<String, String> params, List<File> files, OkHttpResultCallback okHttpResultCallback) {
        Request request = buildPostRequest(url, params, files);
        deliveryResult(okHttpResultCallback, request);
    }

    /**
     * 异步的post请求,无文件传输
     */
    public void postAsync(String url, Map<String, String> params, OkHttpResultCallback okHttpResultCallback) {
        Request request = buildPostRequest(url, params);
        deliveryResult(okHttpResultCallback, request);
    }

    /**
     * 异步的post请求,无文件传输 , 处理重定向
     */
    public void postAsync(String url, Map<String, String> params, boolean isRefer, final OkHttpResultCallback okHttpResultCallback) {
        final Request request = buildPostRequest(url, params);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okHttpResultCallback.onError(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                switch (response.code()) {
                    case 302:
                        okHttpResultCallback.onResponse(Constant.URL_LOGIN.getBytes());
                        break;
                    case 200:
                        okHttpResultCallback.onResponse(Constant.URL_LOGIN.getBytes());
                        break;
                    default:
                        break;
                }
                okHttpResultCallback.onResponse(Constant.ERROR.getBytes());
            }
        });
    }

    /**
     * 构造get请求
     *
     * @param url
     * @param headers
     * @return
     */
    private Request buildGetRequest(String url, Map<String, String> headers) {

        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Set<Map.Entry<String, String>> paramsEntries = headers.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                headersBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        return new Request.Builder().url(url).headers(headersBuilder.build()).build();
    }

    /**
     * 构建post请求参数
     */
    private Request buildPostRequest(String url, Map<String, String> params, List<File> files) {

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (files != null) {
            for (File file : files) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                multipartBodyBuilder.addFormDataPart("file", file.getName(), requestBody);
            }
        }
        MultipartBody multipartBody = multipartBodyBuilder.setType(MultipartBody.FORM).build();
        return new Request.Builder().url(url).post(multipartBody).build();
    }


    /**
     * 构建post请求参数, 无文件传输
     */
    private Request buildPostRequest(String url, Map<String, String> params) {

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = formBodyBuilder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }


    /**
     * 调用call.enqueue，将call加入调度队列，执行完成后在callback中得到结果
     */
    private void deliveryResult(final OkHttpResultCallback okHttpResultCallback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedCallback(call, e, okHttpResultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    switch (response.code()) {
                        case 200:
                            sendSuccessCallback(response.body().bytes(), okHttpResultCallback);
                            break;
                        case 302:
                            sendSuccessCallback(response.body().bytes(), okHttpResultCallback);
                            break;
                        default:
                            sendSuccessCallback(response.body().bytes(), okHttpResultCallback);
                            throw new IOException();
                    }
                } catch (IOException e) {
                    sendFailedCallback(call, e, okHttpResultCallback);
                }
            }
        });
    }

    /**
     * 调用请求失败对应的回调方法，利用handler.post使得回调方法在UI线程中执行
     */
    private void sendFailedCallback(final Call call, final Exception e, final OkHttpResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(call, e);
                }
            }
        });
    }

    /**
     * 调用请求成功对应的回调方法，利用handler.post使得回调方法在UI线程中执行
     */
    private void sendSuccessCallback(final byte[] bytes, final OkHttpResultCallback okHttpResultCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (okHttpResultCallback != null) {
                    okHttpResultCallback.onResponse(bytes);
                }
            }
        });
    }

    /**
     * 取消所有请求，防止进入新界面时，旧界面请求仍在进行，造成阻塞
     */
    public void cancelAllRequest() {
        mOkHttpClient.dispatcher().cancelAll();
    }
}