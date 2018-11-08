package com.poping520.open.mdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * <p>自定义Dialog</p>
 * <p>
 * Created by WangKZ on 18/05/11.
 *
 * @author poping520
 * @version 1.0.0
 */
public class MDialog extends Dialog {

    private static final String TAG = "MDialog";

    private Builder p;

    private LinearLayout mLayout;
    private ScrollView mContentLayout;

    private ImageView mHeaderBg;
    private ImageView mHeaderPic;

    private TextView mTitle;

    private Button mBtnPositive;
    private Button mBtnNegative;
    private Button mBtnNeutral;

    @ColorInt
    private int mAccentColor;


    public MDialog(@NonNull Context context) {
        super(context);
    }

    public MDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private MDialog(Builder builder) {
        this(builder.mContext, R.style.MDialog);
        this.p = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdialog_main);

        mLayout = findViewById(R.id.mdialog_layout);

        mHeaderBg = findViewById(R.id.mdialog_header_bg);
        initHeaderBg();

        mHeaderPic = findViewById(R.id.mdialog_header_pic);
        initHeaderPic();

        mTitle = findViewById(R.id.mdialog_title);
        initTitle();

        mContentLayout = findViewById(R.id.mdialog_content);
        initContentView();

        mBtnPositive = findViewById(R.id.mdialog_positive_btn);
        initPositiveButton();

        mBtnNegative = findViewById(R.id.mdialog_negative_btn);
        initNegativeButton();

        mBtnNeutral = findViewById(R.id.mdialog_neutral_btn);
        initNeutralButton();

        setCancelable(p.mCancelable);
    }

    private void initHeaderBg() {
        if (p.mHeaderBgColorInt != 0) {
            mAccentColor = p.mHeaderBgColorInt;
            mHeaderBg.setBackgroundColor(p.mHeaderBgColorInt);
        }
    }

    private void initHeaderPic() {
        if (p.mHeaderPicRes != 0) mHeaderPic.setImageResource(p.mHeaderPicRes);
        else if (p.mHeaderPicDrawable != null) mHeaderPic.setImageDrawable(p.mHeaderPicDrawable);
    }

    private void initTitle() {
        if (p.mTitle != null) mTitle.setText(p.mTitle);
        if (p.mTitleColor != null) mTitle.setTextColor(p.mTitleColor);
    }

    private void initContentView() {
        if (p.mContentView == null) {
            TextView message = findViewById(R.id.mdialog_content_message);
            initMessage(message);
        } else {
            mContentLayout.removeAllViews();
            mContentLayout.addView(p.mContentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void initMessage(TextView message) {

        if (p.mHTMLMessage != null) {
            // FROM_HTML_MODE_COMPACT: html块元素之间使用一个换行符分隔
            // FROM_HTML_MODE_LEGACY: html块元素之间使用两个换行符分隔
            message.setText(
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            Html.fromHtml(p.mHTMLMessage, Html.FROM_HTML_MODE_COMPACT) :
                            Html.fromHtml(p.mHTMLMessage)
            );
            //超链接
            message.setMovementMethod(LinkMovementMethod.getInstance());
            message.setFocusable(true);
            message.setClickable(true);
        }

        if (p.mMessage != null) message.setText(p.mMessage);
        if (p.mMsgColor != null) message.setTextColor(p.mMsgColor);
    }

    private void initPositiveButton() {
        if (p.mPosBtnText != null) mBtnPositive.setText(p.mPosBtnText);
        else mBtnPositive.setText(R.string.mdialog_confirm);

        //==================设置按钮背景 selector==================//
        float _2dp = Utils.dp2px(p.mContext, 2f);
        int _4dp = (int) Utils.dp2px(p.mContext, 4f);
        int _6dp = (int) Utils.dp2px(p.mContext, 6f);

        GradientDrawable unPressed = new GradientDrawable();
        unPressed.setShape(GradientDrawable.RECTANGLE);
        unPressed.setCornerRadius(_2dp);
        unPressed.setColor(mAccentColor);

        GradientDrawable pressed = new GradientDrawable();
        pressed.setShape(GradientDrawable.RECTANGLE);
        pressed.setCornerRadius(_2dp);
        pressed.setColor(Utils.getDarkerColor(mAccentColor, 0.12f));

        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{-android.R.attr.state_pressed},
                new InsetDrawable(unPressed, _4dp, _6dp, _4dp, _6dp));
        selector.addState(new int[]{android.R.attr.state_pressed},
                new InsetDrawable(pressed, _4dp, _6dp, _4dp, _6dp));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mBtnPositive.setBackground(selector);
        else
            mBtnPositive.setBackgroundDrawable(selector);
        //==================设置按钮背景 selector==================//

        if (Utils.getBrightness(mAccentColor) < 120f) {
            mBtnPositive.setTextColor(Color.WHITE);
        } else {
            mBtnPositive.setTextColor(new Palette.Swatch(mAccentColor, 0).getBodyTextColor());
        }

        mBtnPositive.setOnClickListener(this::onClick);
    }

    private void initNegativeButton() {
        if (p.mNegBtnText != null) mBtnNegative.setText(p.mNegBtnText);
        else mBtnNegative.setText(R.string.mdialog_cancel);

        mBtnNegative.setTextColor(mAccentColor);
        mBtnNegative.setOnClickListener(this::onClick);
    }

    private void initNeutralButton() {
        //如果不设置 text 则不显示该按钮
        if (p.mNeuBtnText == null) mBtnNeutral.setVisibility(View.INVISIBLE);
        else {
            mBtnNeutral.setText(p.mNeuBtnText);
            mBtnNeutral.setTextColor(mAccentColor);
            mBtnNeutral.setOnClickListener(this::onClick);
        }
    }

    //按钮监听
    private void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mdialog_positive_btn) {
            if (p.mPosBtnListener != null)
                p.mPosBtnListener.onClick(this, MDialogAction.POSITIVE);

            if (!p.isClickPosBtnKeepDialog) dismiss();

        } else if (i == R.id.mdialog_negative_btn) {
            if (p.mNegBtnListener != null)
                p.mNegBtnListener.onClick(this, MDialogAction.NEGATIVE);

            if (!p.isClickNegBtnKeepDialog) dismiss();

        } else if (i == R.id.mdialog_neutral_btn) {
            if (p.mNeuBtnListener != null)
                p.mNeuBtnListener.onClick(this, MDialogAction.NEUTRAL);

            if (!p.isClickNeuBtnKeepDialog) dismiss();

        }
    }

    public Button getPositiveButton() {
        return mBtnPositive;
    }

    public Button getNegativeButton() {
        return mBtnNegative;
    }


    public ViewGroup getRootViewGroup() {
        return mLayout;
    }

    public static class Builder {

        private Context mContext;

        @ColorInt
        private int mHeaderBgColorInt;

        @DrawableRes
        private int mHeaderPicRes;

        private Drawable mHeaderPicDrawable;

        private View mContentView;

        private String mTitle;
        private ColorStateList mTitleColor;

        private String mMessage;
        private String mHTMLMessage;
        private ColorStateList mMsgColor;

        private String mPosBtnText, mNegBtnText, mNeuBtnText;
        private OnClickListener mPosBtnListener, mNegBtnListener, mNeuBtnListener;
        //点击按钮是否保持显示Dialog 默认点击任何按钮 dismiss Dialog
        private boolean isClickPosBtnKeepDialog, isClickNegBtnKeepDialog, isClickNeuBtnKeepDialog;

        private boolean mCancelable = true;

        public Builder(@NonNull Context context) {
            mContext = context;
        }


        public Builder setHeaderBgColor(@ColorInt int color) {
            mHeaderBgColorInt = color;
            return this;
        }

        public Builder setHeaderPic(@DrawableRes int resId) {
            mHeaderPicRes = resId;
            return this;
        }

        public Builder setHeaderPic(Drawable drawable) {
            mHeaderPicDrawable = drawable;
            return this;
        }

        /**
         * 设置Dialog的标题
         */
        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        /**
         * 使用resource id设置Dialog标题
         */
        public Builder setTitle(@StringRes int titleId) {
            return setTitle(getString(titleId));
        }

        /**
         * 设置标题字体颜色
         */
        public Builder setTitleColor(@ColorInt int colorInt) {
            return setTitleColor(ColorStateList.valueOf(colorInt));
        }

        /**
         * 设置标题字体颜色
         */
        public Builder setTitleColor(@NonNull ColorStateList color) {
            mTitleColor = color;
            return this;
        }

        /**
         * 设置Dialog内容信息
         */
        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }

        /**
         * 使用resource id设置Dialog内容信息
         */
        public Builder setMessage(@StringRes int messageId) {
            return setMessage(getString(messageId));
        }

        public Builder setMessage(@StringRes int messageId, Object... format) {
            return setMessage(String.format(getString(messageId), format));
        }

        /**
         * 设置含有HTML标签的内容信息
         */
        public Builder setHTMLMessage(String htmlMessage) {
            mHTMLMessage = htmlMessage;
            return this;
        }

        /**
         * 设置含有HTML标签的内容信息
         */
        public Builder setHTMLMessage(@StringRes int htmlMessageId) {
            return setHTMLMessage(getString(htmlMessageId));
        }


        /**
         * 设置标题字体颜色
         */
        public Builder setMessageColor(@ColorInt int colorInt) {
            return setMessageColor(ColorStateList.valueOf(colorInt));
        }

        /**
         * 设置标题字体颜色
         */
        public Builder setMessageColor(@NonNull ColorStateList color) {
            mMsgColor = color;
            return this;
        }

        public Builder setContentView(View contentView) {
            mContentView = contentView;
            return this;
        }

        /**
         * 设置positive按钮的相关属性
         *
         * @param text              按钮名称
         * @param isClickKeepDialog 点击按钮是否保持显示Dialog
         * @param listener          按钮点击监听器
         * @return Builder对象
         */
        public Builder setPositiveButton(String text, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            mPosBtnText = text;
            isClickPosBtnKeepDialog = isClickKeepDialog;
            mPosBtnListener = listener;
            return this;
        }

        /**
         * 设置positive按钮的相关属性
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setPositiveButton(@StringRes int textId, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            return setPositiveButton(getString(textId), isClickKeepDialog, listener);
        }


        /**
         * 设置positive按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setPositiveButton(String text, @Nullable OnClickListener listener) {
            return setPositiveButton(text, false, listener);
        }

        /**
         * 设置positive按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setPositiveButton(@StringRes int textId, @Nullable OnClickListener listener) {
            return setPositiveButton(textId, false, listener);
        }

        /**
         * 设置Negative按钮的相关属性
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNegativeButton(String text, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            mNegBtnText = text;
            isClickNegBtnKeepDialog = isClickKeepDialog;
            mNegBtnListener = listener;
            return this;
        }

        /**
         * 设置Negative按钮的相关属性
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNegativeButton(@StringRes int textId, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            return setNegativeButton(getString(textId), isClickKeepDialog, listener);
        }

        /**
         * 设置Negative按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNegativeButton(String text, @Nullable OnClickListener listener) {
            return setNegativeButton(text, false, listener);
        }

        /**
         * 设置Negative按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNegativeButton(@StringRes int textId, @Nullable OnClickListener listener) {
            return setNegativeButton(textId, false, listener);
        }


        /**
         * 设置Neutral按钮的相关属性
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNeutralButton(String text, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            mNeuBtnText = text;
            isClickNeuBtnKeepDialog = isClickKeepDialog;
            mNeuBtnListener = listener;
            return this;
        }

        /**
         * 设置Neutral按钮的相关属性
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNeutralButton(@StringRes int textId, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            return setNeutralButton(getString(textId), isClickKeepDialog, listener);
        }

        /**
         * 设置Neutral按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNeutralButton(String text, @Nullable OnClickListener listener) {
            return setNeutralButton(text, false, listener);
        }

        /**
         * 设置Neutral按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @return Builder对象
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNeutralButton(@StringRes int textId, @Nullable OnClickListener listener) {
            return setNeutralButton(textId, false, listener);
        }


        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * 创建一个MDialog对象
         *
         * @return Builder对象
         */
        public MDialog create() {
            return new MDialog(this);
        }

        /**
         * 创建一个MDialog对象并显示Dialog
         *
         * @return Builder对象
         */
        public void show() {
            MDialog dialog = create();
            dialog.show();
        }

        /**
         * 返回new Builder时传入的Context对象
         */
        public Context getContext() {
            return mContext;
        }

        private String getString(@StringRes int stringResId) {
            return mContext.getString(stringResId);
        }
    }

    /**
     * <p>MDialog按钮点击监听接口</p>
     */
    public interface OnClickListener {

        /**
         * <p>点击按钮会回调这个方法</p>
         *
         * @param buttonType 按钮类型
         * @see MDialogAction
         */
        void onClick(MDialog dialog, MDialogAction buttonType);
    }
}
