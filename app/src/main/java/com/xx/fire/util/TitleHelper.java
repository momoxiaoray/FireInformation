package com.xx.fire.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xx.fire.R;


/**
 * method: 统一规范标题，按需布局
 * author: Daimc(xiaocheng.ok@qq.com)
 * date: 2020-04-15 10:49
 * description:
 */
public class TitleHelper {
    /**
     * 上下文，创建view的时候需要用到
     */
    private Context mContext;

    /**
     * base view
     */
    private FrameLayout mContentView;

    /**
     * 用户定义的view
     */
    private View mUserView;


    /**
     * 视图构造器
     */
    private LayoutInflater mInflater;

    private int mTitleBarHeight;
    private AppCompatTextView mTitle;

    public ImageView mLeftButton;

    public TextView mRightText;

    private OnBackClick mOnBackClick;

    public ImageView mRightButton;


    /**
     * 沉浸式状态栏
     **/
    private boolean isTransparentStatusBar = false;
    private View titleView;

    public interface OnBackClick {
        void onBack();
    }

    public TitleHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        /*初始化整个内容*/
        initContentView();
        /*初始化用户定义的布局*/
        initUserView(layoutId);
        /*初始化toolbar*/
        initToolBar();
    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        mContentView.setId(R.id.activity_content_layout);
        mContentView.setBackgroundColor(Color.TRANSPARENT);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    private void initToolBar() {
        /*通过inflater获取toolbar的布局文件*/
        titleView = mInflater.inflate(R.layout.layout_title_bar, mContentView);
        mTitle = titleView.findViewById(R.id.page_title);
        mLeftButton = titleView.findViewById(R.id.btn_back);
        mRightButton = titleView.findViewById(R.id.title_right);
        mRightText = titleView.findViewById(R.id.title_right_tx);

        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBackClick != null) {
                    mOnBackClick.onBack();
                } else {
                    ((Activity) mContext).finish();
                }
            }
        });
    }


    private void initUserView(int id) {
        mUserView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mTitleBarHeight = (int) mContext.getResources().getDimension(R.dimen.dp_48);
        if (isCanTransparentStatusBar()) {
            params.topMargin = mTitleBarHeight + BarUtils.getStatusBarHeight();
        } else {
            params.topMargin = mTitleBarHeight;
        }
        mContentView.addView(mUserView, params);
    }

    public void dealContentView() {
        if (!StringUtils.isEmpty(mTitle.getText().toString())) {
            FrameLayout.LayoutParams titleParams = (FrameLayout.LayoutParams) titleView.getLayoutParams();
            titleParams.topMargin = BarUtils.getStatusBarHeight();
            titleView.setLayoutParams(titleParams);
        }
    }

    /**
     * 解决透明状态栏后无法顶起布局的问题
     */
    public void setContentViewFitsSystemWindows() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (!StringUtils.isEmpty(mTitle.getText().toString())) {
            FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, mTitleBarHeight + BarUtils.getStatusBarHeight());
            titleView.setLayoutParams(titleParams);
            titleView.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        }
        params.topMargin = mTitleBarHeight;
        mUserView.setLayoutParams(params);
        mUserView.setFitsSystemWindows(true);
    }

    /**
     * 监听返回键
     *
     * @param onBackClickListener
     */
    public void setOnBackClickListener(OnBackClick onBackClickListener) {
        this.mOnBackClick = onBackClickListener;
    }

    /**
     * 监听返回键
     *
     * @param onBackClickListener
     */
    public void setOnBackClickListener(int leftBtn, OnBackClick onBackClickListener) {
        mLeftButton.setImageResource(leftBtn);
        this.mOnBackClick = onBackClickListener;
    }

    /**
     * 设置标题栏右边按钮
     *
     * @param rightBtn
     * @param listener
     */
    public void setOnRightClickListener(int rightBtn, View.OnClickListener listener) {
        mRightButton.setVisibility(View.VISIBLE);
        mRightButton.setImageResource(rightBtn);
        mRightButton.setOnClickListener(listener);
    }

    /**
     * 设置标题栏右边按钮
     *
     * @param rightTx
     * @param listener
     */
    public void setOnRightTxClickListener(String rightTx, View.OnClickListener listener) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(rightTx);
        mRightText.setOnClickListener(listener);
    }

    /**
     * 设置标题栏右边按钮 并添加背景
     *
     * @param rightTx
     * @param listener
     */
    public void setOnRightTxClickListener(String rightTx, int background, View.OnClickListener listener) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(rightTx);
        mRightText.setBackgroundResource(background);
        mRightText.setOnClickListener(listener);

    }

    /**
     * 设置右边按钮文字颜色和背景
     */
    public void setOnRightTxtColorAndBackground(int color, int background) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setTextColor(color);
        mRightText.setBackgroundResource(background);
    }

    public void hideLeftButton() {
        mLeftButton.setVisibility(View.INVISIBLE);
    }

    public void setRightText(String text) {
        mRightText.setText(text);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(int title) {
        mTitle.setText(mContext.getString(title));
    }

    /**
     * 是否透明状态栏(沉浸式状态栏)
     *
     * @return
     */
    public boolean isCanTransparentStatusBar() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) && isTransparentStatusBar;
    }

    public FrameLayout getContentView() {
        return mContentView;
    }
}
