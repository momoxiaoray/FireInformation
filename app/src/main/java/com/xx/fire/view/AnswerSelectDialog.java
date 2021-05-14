package com.xx.fire.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.model.QuestionAnswer;
import com.xx.fire.model.QuestionAnswerUser;
import com.xx.fire.util.T;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * desc   :答案弹窗
 */
public class AnswerSelectDialog extends Dialog {

    public interface OnSelectListener {
        void onSelect(QuestionAnswer answer);
    }

    private RadioGroup radioGroup;
    public List<QuestionAnswer> answers = new ArrayList<>();
    public OnSelectListener onSelectListener;

    private AnswerSelectDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_select_dialog);
        radioGroup = findViewById(R.id.radio_group);
    }

    @Override
    public void show() {
        super.show();
        show(this);
    }

    /**
     * @param dialog
     */
    private void show(AnswerSelectDialog dialog) {
        dialog.radioGroup.removeAllViews();
        for (int i = 0; i < dialog.answers.size(); i++) {
            RadioButton radioButton = new RadioButton(dialog.getContext());
            radioButton.setTag(i);
            radioButton.setGravity(Gravity.CENTER_VERTICAL);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            radioButton.setText(dialog.answers.get(i).getAnswer_content());
            radioButton.setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.grey_700));
            RadioGroup.LayoutParams layoutParams= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.rightMargin = (int) getContext().getResources().getDimension(R.dimen.dp_12);
            layoutParams.topMargin = (int) getContext().getResources().getDimension(R.dimen.dp_8);
            radioButton.setLayoutParams(layoutParams);
            dialog.radioGroup.addView(radioButton);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!compoundButton.isPressed()) {
                        return;
                    }
                    if (b) {
                        if (dialog.onSelectListener != null) {
                            dialog.onSelectListener.onSelect(answers.get((Integer) compoundButton.getTag()));
                        }
                    }
                }
            });
        }
    }

    public static class Builder {
        private AnswerSelectDialog dialog;

        public Builder(Context context) {
            dialog = new AnswerSelectDialog(context);
        }

        public void cancel() {
            dialog.cancel();
        }

        /**
         * 设置内容
         */
        public Builder setData(List<QuestionAnswer> answers) {
            dialog.answers = answers;
            return this;
        }

        public Builder setOnSelectListener(OnSelectListener listener) {
            dialog.onSelectListener = listener;
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public AnswerSelectDialog create() {
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER); //可设置dialog的位置
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = ScreenUtils.getScreenWidth() / 6 * 5;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.dimAmount = 0f;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(lp);
            return dialog;
        }
    }


}
