package com.example.thatnight.wanandroid.view.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;


import com.example.thatnight.wanandroid.entity.ArticleComment;
import com.example.thatnight.wanandroid.utils.AccountUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lujianchao on 2017/1/19.
 *
 * @author lujianchao
 */

public class CommentListTextView extends AppCompatTextView {
    /**
     * 所有评论数据
     */
    private List<ArticleComment> mInfos = new ArrayList<>();
    private onCommentListener mListener;

    /**
     * 点击文字会触发两个回调，用这个变量控制屏蔽掉一个
     */
    private boolean isNickNameClick = false;


    /**
     * 最大显示行数，超过指定行数多一行显示为查看更多文本，可设置文本
     */
    private int mMaxlines = 12;
    /**
     * 当超过n行后，n+1行显示为这个文本；
     */
    private String mMoreStr = "查看全部评论";
    /**
     * 谁回复谁中回复字符串，可以变成别的
     */
    private String mTalkStr = "回复";
    /**
     * 人名称颜色
     */
    private int mNameColor = Color.parseColor("#1797ff");

    private int mToNameColor = Color.parseColor("#ff174d");

    private int mCommentColor = Color.parseColor("#161616");

    private int mTalkColor = Color.parseColor("#161616");

    public int getTalkColor() {
        return mTalkColor;
    }

    public CommentListTextView setTalkColor(final int mTalkColor) {
        this.mTalkColor = mTalkColor;
        return this;
    }

    public int getMaxlines() {
        return mMaxlines;
    }

    public CommentListTextView setMaxlines(final int mMaxlines) {
        this.mMaxlines = mMaxlines;
        return this;
    }

    public String getMoreStr() {
        return mMoreStr;
    }

    public CommentListTextView setMoreStr(final String mMoreStr) {
        this.mMoreStr = mMoreStr;
        return this;
    }

    public String getTalkStr() {
        return mTalkStr;
    }

    public CommentListTextView setTalkStr(final String mTalkStr) {
        this.mTalkStr = mTalkStr;
        return this;
    }

    public int getNickNameColor() {
        return mNameColor;
    }

    public CommentListTextView setNameColor(final int mNameColor) {
        this.mNameColor = mNameColor;
        return this;
    }

    public int getContentColor() {
        return mCommentColor;
    }

    public CommentListTextView setCommentColor(final int mCommentColor) {
        this.mCommentColor = mCommentColor;
        return this;
    }

    public CommentListTextView(final Context context) {
        super(context);
    }


    public CommentListTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentListTextView setonCommentListener(final onCommentListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public void setData(List<ArticleComment> infos) {
        mInfos.clear();
        mInfos.addAll(infos);
        /**
         * textview必须设置，否则自定义点击事件没响应
         */
        setHighlightColor(Color.TRANSPARENT);
        setText(getContentString());
        setMovementMethod(LinkMovementMethod.getInstance());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isNickNameClick) {
                    isNickNameClick = false;
                    return;
                }
                if (mListener != null) {
                    mListener.onOtherClick();
                }
            }
        });
    }

    private SpannableStringBuilder getContentString() {
        SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();
        /**
         * 对评论数据进行处理，默认超过mMaxlines条则第mMaxlines+1条显示查看全部
         */
        for (int mI = 0; mI < mInfos.size(); mI++) {
            final ArticleComment mInfo = mInfos.get(mI);
            /**
             * 拼接成“张三：今天天气真好”这种形式，就是一行显示的文本。
             * 或者为张三 回复 李四：今天天气真好
             */
            String mS = null;
            if (mInfo.getToAccount() == null || mInfo.getToAccount().getNickName() == null || "".equals(mInfo.getToAccount().getNickName())) {
                mS = mInfo.getAccount().getNickName() + "：" + mInfo.getContent();
            } else {
                mS = mInfo.getAccount().getNickName() + mTalkStr + mInfo.getToAccount().getNickName() + "：" + mInfo.getContent();
            }
            /**
             * 获得一个SpannableString文本的对象
             */
            SpannableString mString = new SpannableString(mS);
            /**
             * 我们假设“张三：今天天气真好”这句话中“张三：”这三个字符为橘红色字体并且添加点击事件，其余评论内容单独添加事件，一般业务需求是点击人名跳到个人主页或者聊天，点击评论内容是对这条评论进行评论。
             * 谁回复谁同理，就不写了
             */
            int start = 0;
            int end = (mInfo.getAccount().getNickName()).length();
            final int finalMI = mI;
            /**
             * 处理第一个人名
             * 设置文本从第0个开始到end位置具有点击事件，点击后回调，updateDrawState中设置文本从第0个到第end位置的文本前景色就是文字颜色为橘红色
             */

            mString.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    /**
                     * 是否有下划线
                     */
                    ds.setUnderlineText(false);
                    /**
                     * 橘红色字体
                     */
                    if (AccountUtil.getBmobAccount().getObjectId().equals(mInfo.getAccount().getObjectId())) {
                        ds.setColor(mNameColor);
                    } else {
                        ds.setColor(mToNameColor);
                    }
                }

                @Override
                public void onClick(final View widget) {
                    isNickNameClick = true;
                    if (mListener != null) {
                        mListener.onNickNameClick(finalMI, mInfos.get(finalMI));
                    }
                }
            }, 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            mString.setSpan(new ForegroundColorSpan(mTalkColor), mInfo.getAccount().getNickName().length(), mInfo.getAccount().getNickName().length() + mTalkStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            /**
             * 处理第二个人名
             */
            if (mInfo.getToAccount() != null && !TextUtils.isEmpty(mInfo.getToAccount().getNickName())) {
                start = mInfo.getAccount().getNickName().length() + mTalkStr.length();
                end = mInfo.getAccount().getNickName().length() + mTalkStr.length() + mInfo.getToAccount().getNickName().length();


                mString.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        /**
                         * 是否有下划线
                         */
                        ds.setUnderlineText(false);
                        /**
                         * 橘红色字体
                         */
                        if (AccountUtil.getBmobAccount().getObjectId().equals(mInfo.getToAccount().getObjectId())) {
                            ds.setColor(mNameColor);
                        } else {
                            ds.setColor(mToNameColor);
                        }
                    }

                    @Override
                    public void onClick(final View widget) {
                        isNickNameClick = true;
                        if (mListener != null) {
                            mListener.onToNickNameClick(finalMI, mInfos.get(finalMI));
                        }
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            /**
             * 设置文本从第end个开始到最后位置具有点击事件，点击后回调，updateDrawState中设置文本从第end个到最后的文本前景色就是文字颜色为黑色
             */
            mString.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    /**
                     * 是否有下划线
                     */
                    ds.setUnderlineText(false);
                    /**
                     * 黑色字体
                     */
                    ds.setColor(mCommentColor);
                }

                @Override
                public void onClick(final View widget) {
                    isNickNameClick = true;
                    if (mListener != null) {
                        mListener.onCommentItemClick(finalMI, mInfos.get(finalMI));
                    }
                }
            }, end, mS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            /**
             * 设置文本从第end个到最后的文本前景色就是文字颜色为黑色，即评论内容为黑色
             */

            mStringBuilder.append(mString);
            mStringBuilder.append("\r\n");
            if (mI == (mMaxlines - 1)) {
                break;
            }
        }
        if (mInfos.size() > mMaxlines) {
            mStringBuilder.append("查看全部评论");
        } else {
            /**
             * 如果不大于3条，则删除最后的换行
             */
            mStringBuilder.delete(mStringBuilder.length() - 2, mStringBuilder.length());
        }
        return mStringBuilder;
    }


    public interface onCommentListener {
        /**
         * 点击人名的回调
         *
         * @param position 第几条评论  从0开始
         * @param mInfo 评论相关信息
         */
        public void onNickNameClick(int position, ArticleComment mInfo);

        /**
         * 点击被评论人名的回调
         *
         * @param position 第几条评论  从0开始
         * @param mInfo 评论相关信息
         */
        public void onToNickNameClick(int position, ArticleComment mInfo);

        /**
         * 点击评论文本回调，主要针对对谁回复操作
         *
         * @param position
         * @param mInfo
         */
        public void onCommentItemClick(int position, ArticleComment mInfo);

        /**
         * 点击非文字部分
         */
        public void onOtherClick();
    }

}
