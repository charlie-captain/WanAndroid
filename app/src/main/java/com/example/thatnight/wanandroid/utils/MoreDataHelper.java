package com.example.thatnight.wanandroid.utils;

import com.example.thatnight.wanandroid.constant.Constant;
import com.example.thatnight.wanandroid.entity.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MoreDataHelper {

    public static List<Article> html2ArticleList(int mode, String response) {
        List<Article> articles = null;
        switch (mode) {
            case Constant.MODE_MORE_TOUTIAO:
                articles = getToutiao(response);
                break;
            case Constant.MODE_MORE_MEITUAN:
                articles = getMeituan(response);
                break;
            case Constant.MODE_MORE_WANGYI:
                articles = getWangyi(response);
                break;
            case Constant.MODE_MORE_BUS:
                articles = getBus(response);
                break;
            case Constant.MODE_MORE_GITYUAN:
                articles = getGityuan(response);
                break;
            default:
                break;
        }
        return articles;
    }

    /**
     * 开发者头条
     *
     * @param response
     * @return
     */
    public static final List<Article> getToutiao(String response) {
        List<Article> articles = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Element dayElement = document.selectFirst("h3.date");
        String day = dayElement.selectFirst("small").text();

        Elements elements = document.getElementsByClass("post");
        for (Element e : elements) {
            Element content = e.selectFirst("div.content");
            Element contentAttr = content.selectFirst("a");
            String url = Constant.URL_TOUTIAO + contentAttr.attr("href");
            String title = contentAttr.text();
            Element userElements = e.selectFirst("div.user-avatar");
            Element user = userElements.selectFirst("img");
            String userName = user.attr("alt").split(" - ")[0];
            String userIconUrl = user.attr("src");
            Article article = new Article();
            article.setTitle(title);
            article.setLink(url);
            article.setAuthor(userName);
            article.setAuthorImg(userIconUrl);
            article.setOther(true);
            article.setNiceDate(day);
            articles.add(article);
        }
        return articles;
    }

    /**
     * 美团
     *
     * @param response
     * @return
     */
    public static final List<Article> getMeituan(String response) {
        List<Article> articles = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.getElementsByClass("post");
        for (Element e : elements) {
            Element content = e.selectFirst("header.post-title");
            Element contentAttr = content.selectFirst("a");
            String url = Constant.URL_MEITUAN + contentAttr.attr("href");
            String title = contentAttr.text();
            Element userElements = e.selectFirst("span.post-meta-author");
            Element timeElements = e.selectFirst("span.post-meta-ctime");
            String userName = userElements.text();
            String time = timeElements.text();
            Article article = new Article();
            article.setTitle(title);
            article.setLink(url);
            article.setAuthor(userName);
            article.setNiceDate(time);
            article.setOther(true);
            articles.add(article);
        }
        return articles;
    }

    /**
     * 网易
     *
     * @param response
     * @return
     */
    public static final List<Article> getWangyi(String response) {
        List<Article> articles = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.getElementsByClass("post-preview");
        for (Element e : elements) {
            Element contentAttr = e.selectFirst("a");
            String url = Constant.URL_WANGYI + contentAttr.attr("href");
            Element content = e.selectFirst("h2.post-title");
            String title = content.text();
            Element userElements = e.selectFirst("p.post-meta");
            String[] post = userElements.text().split(" ");
            String userName = post[2];
            String time = post[6] + "-" + post[4] + "-" + post[5].substring(0, post[5].indexOf(","));
            Article article = new Article();
            article.setTitle(title);
            article.setLink(url);
            article.setAuthor(userName);
            article.setNiceDate(time);
            article.setOther(true);
            articles.add(article);
        }
        return articles;
    }

    /**
     * AndroidBUS
     *
     * @param response
     * @return
     */
    public static final List<Article> getBus(String response) {
        List<Article> articles = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.getElementsByClass("row");
        for (Element e : elements) {
            Element contentAttr = e.selectFirst("a");
            String url = Constant.URL_BUS + contentAttr.attr("href");
            Element content = e.selectFirst("h2");
            String title = content.text();
            Element userElements = e.selectFirst("div.info");
            String userIcon = userElements.select("img").attr("src");
            Elements userOtherElements = userElements.select("span");
            String userName = userOtherElements.get(0).text();
            String time = userOtherElements.get(2).attr("title");

            Article article = new Article();
            article.setTitle(title);
            article.setLink(url);
            article.setAuthor(userName);
            article.setNiceDate(time);
            article.setAuthorImg(userIcon);
            article.setOther(true);
            articles.add(article);
        }
        return articles;
    }

    /**
     * GitYuan
     *
     * @param response
     * @return
     */
    public static final List<Article> getGityuan(String response) {
        List<Article> articles = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.getElementsByClass("post-preview");
        for (Element e : elements) {
            Element contentAttr = e.selectFirst("a");
            String urlNext = contentAttr.attr("href");
            String url = Constant.URL_GITYUAN;
            if (urlNext.contains(Constant.URL_GITYUAN)) {
                url = urlNext;
            } else {
                url += urlNext;
            }
            Element content = e.selectFirst("h2");
            String title = content.text();

            Element userElements = e.selectFirst("p.post-meta");
            String[] post = userElements.text().split(" ");
            String userName = post[2];
            String time = post[6] + "-" + post[4] + "-" + post[5].substring(0, post[5].indexOf(","));
            Article article = new Article();
            article.setTitle(title);
            article.setLink(url);
            article.setAuthor(userName);
            article.setNiceDate(time);
            article.setOther(true);
            articles.add(article);
        }
        return articles;
    }
}

