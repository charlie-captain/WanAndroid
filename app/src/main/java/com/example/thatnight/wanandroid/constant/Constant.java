package com.example.thatnight.wanandroid.constant;

/**
 * Created by thatnight on 2017.11.1.
 */

public class Constant {

    public static final String URL_BASE = "http://www.wanandroid.com";


    public static final String URL_COLLECT = "/lg/collect/";
    public static final String URL_UNCOLLECT = "/lg/uncollect_originId/";
    public static final String URL_ARTICLE = "/article/list/";


    public static final String URL_ARTICLE_COLLECT = "/lg/collect/list/";
    public static final String URL_COLLECT_UNCOLLECT = "/lg/uncollect/";

    public static final String URL_TREE = "/tree/json";

    public static final String URL_HOT_SEARCH = "/hotkey/json";

    /*
        post
     */
    public static final String URL_SEARCH = "/article/query/";   //   /0/json?k=?  post
    public static final String URL_LOGIN = "/user/login";
    public static final String URL_REGISTER = "/user/register/";

    /*
        activity requestcode
     */
    public static final int REQUEST_LOGIN = 19;


    /*
        EventBus post
     */
    public static final String REFRESH = "refresh";
    public static final String REFRESH_NEWS = "refresh_news";
    public static final String TOP_NEWS = "scroll_top_news";
    public static final String TOP_CLASSIFY = "scroll_top_classify";
    public static final String SWITCH_TO_CLASSIFY = "switch_to_classify";

    public static final String UPDATE_DIALOG = "update_app_1.4.1";
}
