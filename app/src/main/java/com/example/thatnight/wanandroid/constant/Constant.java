package com.example.thatnight.wanandroid.constant;

/**
 * Created by thatnight on 2017.11.1.
 */

public class Constant {

    /*wanandroid*/
    public static final String URL_BASE = "http://www.wanandroid.com/";

    public static final String URL_COLLECT = "lg/collect/";
    public static final String URL_UNCOLLECT = "lg/uncollect_originId/";
    public static final String URL_ARTICLE = "article/list/";
    public static final String URL_PROJECT = "project/tree/json";
    public static final String URL_PROJECT_LIST = "project/list/";
    public static final String URL_ARTICLE_COLLECT = "lg/collect/list/";
    public static final String URL_COLLECT_UNCOLLECT = "lg/uncollect/";
    public static final String URL_TREE = "tree/json";
    public static final String URL_HOT_SEARCH = "hotkey/json";
    public static final String URL_BANNER = "banner/json";
    public static final String URL_WECHAT = "wxarticle/chapters/json";
    public static final String URL_WECHAT_LIST = "wxarticle/list/";

    /*
          post
       */
    public static final String URL_SEARCH = "/article/query/";   //   /0/json?k=?  post
    public static final String URL_LOGIN = "/user/login";
    public static final String URL_REGISTER = "/user/register/";
    public static final String URL_CHANGE_PWD = "/user/lg/password";



    /*url_ more*/
    public static final String URL_TOUTIAO = "https://toutiao.io/";
    public static final String URL_TOUTIAO_PREV = "https://toutiao.io/prev/";
    public static final String URL_MEITUAN = "https://tech.meituan.com/";
    public static final String URL_MEITUAN_TAG = "https://tech.meituan.com/tag/Android/";
    public static final String URL_WANGYI = "https://blog.klmobile.app/";
    public static final String URL_BUS_BLOG = "http://www.apkbus.com/blog/";
    public static final String URL_BUS = "http://www.apkbus.com/";

    public static final String URL_GITYUAN = "http://gityuan.com/";


    /*mode_more*/
    public static final int MODE_MORE_TOUTIAO = 1330;
    public static final int MODE_MORE_MEITUAN = 1331;
    public static final int MODE_MORE_WANGYI = 1332;
    public static final int MODE_MORE_BUS = 1333;
    public static final int MODE_MORE_GITYUAN = 1334;



    /*
        activity requestcode
     */
    public static final int REQUEST_LOGIN = 19;


    /*
        EventBus post
     */
    public static final String REFRESH = "refresh";
    public static final String REFRESH_NEWS = "refresh_news";
    public static final String REFRESH_COLLECT = "refresh_collect";
    public static final String TOP_NEWS = "scroll_top_news";
    public static final String TOP_CLASSIFY = "scroll_top_classify";
    public static final String SWITCH_TO_CLASSIFY = "switch_to_classify";
    public static final String MORE_REFRESH_NEWS = "refresh_more";
    public static final String MORE_TOP_NEWS = "scroll_top_more";

    public static final String ERROR = "error";
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = -1;


    public static final String STRING_ERROR = "服务器出现错误!";

}
