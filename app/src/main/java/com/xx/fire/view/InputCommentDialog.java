package com.xx.fire.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.xx.fire.R;
import com.xx.fire.util.T;


/**
 * desc   :输入框弹窗
 */
public class InputCommentDialog extends Dialog {

    public interface InputDialogBtnClickListener {
        void onClick(String content);
    }

    private TextView btnSave;
    private EditText etContent;
    private InputDialogBtnClickListener InputDialogBtnClickListener;
    private String message = "";
    private String messageHit = "";

    private InputCommentDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_comment_dialog);
        btnSave = findViewById(R.id.btn_comment);
        etContent = findViewById(R.id.edit_content);
    }

    @Override
    public void show() {
        super.show();
        show(this);
    }

    /**
     * @param InputDialog
     */
    private void show(InputCommentDialog InputDialog) {
        etContent.setText(InputDialog.message);
        etContent.setSelection(InputDialog.message.length());
        etContent.setHint(InputDialog.messageHit);
        btnSave.setOnClickListener(v -> {
            dismiss();
            if (InputDialogBtnClickListener != null)
                InputDialogBtnClickListener.onClick(etContent.getText().toString().trim());

        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 100) {
                    editable.delete(editable.length() - 1, editable.length());
                    T.showToast("评论超过字数啦");
                }
            }
        });
        etContent.requestFocus();
        etContent.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager im = (InputMethodManager) etContent
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(etContent, 0);
            }
        });
    }

    public static class Builder {
        private InputCommentDialog InputDialog;

        public Builder(Context context) {
            InputDialog = new InputCommentDialog(context);
        }

        /**
         * 设置内容
         *
         * @param msg
         */
        public Builder setMessage(String msg) {
            InputDialog.message = msg;
            return this;
        }

        /**
         * 设置输入框里面的提示
         * @param hit
         * @return
         */
        public Builder setMessageHit(String hit) {
            InputDialog.messageHit = hit;
            return this;
        }

        public Builder cancel() {
            InputDialog.cancel();
            return this;
        }

        /**
         * 确定点击
         *
         * @param listener
         * @return
         */
        public Builder setSureClickListener(InputDialogBtnClickListener listener) {
            InputDialog.InputDialogBtnClickListener = listener;
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public InputCommentDialog create() {
            Window window = InputDialog.getWindow();
            window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.dimAmount = 0f;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(lp);
            return InputDialog;
        }
    }
}
