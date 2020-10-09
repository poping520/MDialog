package com.poping520.open.mdialog;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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

    private Context mContext;

    private LinearLayout mLayout;
    private ScrollView mContentLayout;

    private ImageView mHeaderBg;
    private ImageView mHeaderPic;

    private TextView mTitle, mContentMessage;

    private Button mBtnPositive, mBtnNegative, mBtnNeutral;

    private OnClickListener mListener;

    @ColorInt
    private int mAccentColor;

    private MDialog(Builder builder) {
        super(builder.mContext, R.style.MDialog);
        this.p = builder;
        this.mContext = builder.mContext;

        initDialog();
    }

    private void initDialog() {
        setContentView(R.layout.mdialog_main);

        mLayout = findViewById(R.id.mdialog_layout);

        mHeaderBg = findViewById(R.id.mdialog_header_bg);
        initHeaderBg();

        mHeaderPic = findViewById(R.id.mdialog_header_pic);
        initHeaderPic();

        mTitle = findViewById(R.id.mdialog_title);
        setTitle(p.mTitle);
        if (p.mTitleColor != null) setTitleColor(p.mTitleColor);

        mContentLayout = findViewById(R.id.mdialog_content);
        mContentMessage = findViewById(R.id.mdialog_content_message);
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

    public void setTitle(@StringRes int titleId) {
        setTitle(mContext.getString(titleId));
    }

    public void setTitle(@Nullable CharSequence title) {
        mTitle.setText(title);
    }

    public void setTitleColor(@ColorInt int colorId) {
        setTitleColor(ColorStateList.valueOf(colorId));
    }

    public void setTitleColor(@NonNull ColorStateList color) {
        mTitle.setTextColor(color);
    }

    private void initContentView() {
        if (p.mContentView == null) {
            initMessage();
        } else {
            mContentLayout.removeAllViews();
            mContentLayout.addView(p.mContentView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            );
        }
    }

    private void initMessage() {
        if (p.mHTMLMessage != null) setHTMLMessage(p.mHTMLMessage);
        if (p.mMessage != null) setMessage(p.mMessage);
        if (p.mMsgColor != null) mContentMessage.setTextColor(p.mMsgColor);
    }

    public void setMessage(@StringRes int messageId) {
        setMessage(mContext.getString(messageId));
    }

    public void setMessage(CharSequence message) {
        mContentMessage.setText(message);
    }

    public void setHTMLMessage(@StringRes int htmlMessageId) {
        setHTMLMessage(mContext.getString(htmlMessageId));
    }

    public void setHTMLMessage(String htmlMessage) {
        // FROM_HTML_MODE_COMPACT: html块元素之间使用一个换行符分隔
        // FROM_HTML_MODE_LEGACY: html块元素之间使用两个换行符分隔
        mContentMessage.setText(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                        Html.fromHtml(htmlMessage, Html.FROM_HTML_MODE_COMPACT) :
                        Html.fromHtml(htmlMessage)
        );
        //超链接
        mContentMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initPositiveButton() {
        if (p.mPosBtnText != null) mBtnPositive.setText(p.mPosBtnText);
        else mBtnPositive.setText(R.string.mdialog_confirm);

        mBtnPositive.setOnClickListener(this::onClick);


        //================== 按钮背景 selector ==================//

        // 四角弧度
        final float _2dp = Utils.dp2px(p.mContext, 2f);
        final int _4dp = (int) Utils.dp2px(p.mContext, 4f);
        final int _6dp = (int) Utils.dp2px(p.mContext, 6f);
        final int _12dp = (int) Utils.dp2px(p.mContext, 12f);

        final GradientDrawable unPressed = new GradientDrawable();
        final GradientDrawable pressed = new GradientDrawable();

        unPressed.setCornerRadius(_2dp);
        pressed.setCornerRadius(_2dp);

        unPressed.setShape(GradientDrawable.RECTANGLE);
        pressed.setShape(GradientDrawable.RECTANGLE);

        unPressed.setColor(mAccentColor);
        pressed.setColor(Utils.getDarkerColor(mAccentColor, 0.12f));

        final StateListDrawable selector = new StateListDrawable();
        selector.addState(
                new int[]{-android.R.attr.state_pressed},
                new InsetDrawable(unPressed, _4dp, _6dp, _4dp, _6dp)
        );
        selector.addState(
                new int[]{android.R.attr.state_pressed},
                new InsetDrawable(pressed, _4dp, _6dp, _4dp, _6dp)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mBtnPositive.setBackground(selector);
        else
            mBtnPositive.setBackgroundDrawable(selector);

        mBtnPositive.setPadding(_12dp, 0, _12dp, 0);
        //================== 按钮背景 selector ==================//


        //================== 按钮浮动动画 ==================//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int animTime = 150;
            final String propertyName = "translationZ";

            final ObjectAnimator unPressedAnim = new ObjectAnimator();
            unPressedAnim.setDuration(animTime);
            unPressedAnim.setInterpolator(new AccelerateInterpolator());
            unPressedAnim.setPropertyName(propertyName);
            unPressedAnim.setFloatValues((float) _4dp);

            final ObjectAnimator pressedAnim = new ObjectAnimator();
            pressedAnim.setDuration(animTime);
            pressedAnim.setInterpolator(new DecelerateInterpolator());
            pressedAnim.setPropertyName(propertyName);
            pressedAnim.setFloatValues(0f);

            final StateListAnimator sinkAnim = new StateListAnimator();
            sinkAnim.addState(
                    new int[]{-android.R.attr.state_pressed}, unPressedAnim
            );
            sinkAnim.addState(
                    new int[]{android.R.attr.state_pressed}, pressedAnim
            );
            mBtnPositive.setStateListAnimator(sinkAnim);
        }
        //================== 按钮浮动动画 ==================//


        //================== 按钮字体颜色 ==================//
        if (Utils.getBrightness(mAccentColor) < 120f) {
            mBtnPositive.setTextColor(Color.WHITE);
        } else {
            mBtnPositive.setTextColor(new Palette.Swatch(mAccentColor, 0).getBodyTextColor());
        }
        //================== 按钮字体颜色 ==================//
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
        final int id = v.getId();
        MDialogAction action;

        if (id == R.id.mdialog_positive_btn) {
            action = MDialogAction.POSITIVE;

            if (p.mPosBtnListener != null)
                p.mPosBtnListener.onClick(this, MDialogAction.POSITIVE);

            if (!p.isClickPosBtnKeepDialog) dismiss();

        } else if (id == R.id.mdialog_negative_btn) {
            action = MDialogAction.NEGATIVE;

            if (p.mNegBtnListener != null)
                p.mNegBtnListener.onClick(this, MDialogAction.NEGATIVE);

            if (!p.isClickNegBtnKeepDialog) dismiss();

        } else {
            action = MDialogAction.NEUTRAL;

            if (p.mNeuBtnListener != null)
                p.mNeuBtnListener.onClick(this, MDialogAction.NEUTRAL);

            if (!p.isClickNeuBtnKeepDialog) dismiss();
        }

        if (mListener != null) {
            mListener.onClick(this, action);
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public Button getPositiveButton() {
        return mBtnPositive;
    }

    public Button getNegativeButton() {
        return mBtnNegative;
    }

    public Button getNeutralButton() {
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

        public Builder setHeaderBgColorInt(@ColorInt int color) {
            mHeaderBgColorInt = color;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public Builder setHeaderBgColorRes(@ColorRes int colorId, Resources.Theme theme) {
            return setHeaderBgColorInt(mContext.getResources().getColor(colorId, theme));
        }

        public Builder setHeaderBgColorRes(@ColorRes int colorId) {
            return setHeaderBgColorInt(mContext.getResources().getColor(colorId));
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
        public Builder setTitle(@Nullable String title) {
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
         * 设置含有HTML标签的内容信息
         */
        public Builder setHTMLMessage(String htmlMessage, Object... format) {
            return setHTMLMessage(String.format(htmlMessage, format));
        }

        /**
         * 设置含有HTML标签的内容信息
         */
        public Builder setHTMLMessage(@StringRes int htmlMessageId, Object... format) {
            return setHTMLMessage(getString(htmlMessageId), format);
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
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setPositiveButton(@StringRes int textId, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            return setPositiveButton(getString(textId), isClickKeepDialog, listener);
        }

        /**
         * 设置positive按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setPositiveButton(String text, @Nullable OnClickListener listener) {
            return setPositiveButton(text, false, listener);
        }

        /**
         * 设置positive按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setPositiveButton(@StringRes int textId, @Nullable OnClickListener listener) {
            return setPositiveButton(textId, false, listener);
        }

        /**
         * 设置Negative按钮的相关属性
         *
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
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNegativeButton(@StringRes int textId, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            return setNegativeButton(getString(textId), isClickKeepDialog, listener);
        }

        /**
         * 设置Negative按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNegativeButton(String text, @Nullable OnClickListener listener) {
            return setNegativeButton(text, false, listener);
        }

        /**
         * 设置Negative按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNegativeButton(@StringRes int textId, @Nullable OnClickListener listener) {
            return setNegativeButton(textId, false, listener);
        }

        /**
         * 设置Neutral按钮的相关属性
         *
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
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNeutralButton(@StringRes int textId, boolean isClickKeepDialog, @Nullable OnClickListener listener) {
            return setNeutralButton(getString(textId), isClickKeepDialog, listener);
        }

        /**
         * 设置Neutral按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNeutralButton(String text, @Nullable OnClickListener listener) {
            return setNeutralButton(text, false, listener);
        }

        /**
         * 设置Neutral按钮的相关属性
         * 如果使用这个方法，则默认点击按钮后使Dialog消失
         *
         * @see #setPositiveButton(String, boolean, OnClickListener)
         */
        public Builder setNeutralButton(@StringRes int textId, @Nullable OnClickListener listener) {
            return setNeutralButton(textId, false, listener);
        }

        /**
         * 屏蔽返回按钮
         */
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * 创建一个MDialog对象
         */
        public MDialog create() {
            return new MDialog(this);
        }

        /**
         * 创建一个MDialog对象并显示Dialog
         */
        public void show() {
            create().show();
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
