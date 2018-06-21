package com.yongxiang.verificationview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


/**
 * Created by dingyongxiang on 2018/5/25.
 * Email:978024512@qq.com
 */

public class VerificationCodeInput extends LinearLayout {

    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";

    private static final String TAG = "VerificationCodeInput";
    private int box = 6;
    private float textSize = 15;
    private int boxWidth = 120;
    private int boxHeight = 120;
    private int childHPadding = 0;
    private int childVPadding = 0;
    private String inputType = TYPE_PASSWORD;
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;
    private Listener listener;

    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeInput);
        box = a.getInt(R.styleable.vericationCodeInput_box, 6);
        childHPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.vericationCodeInput_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.vericationCodeInput_box_bg_normal);
        inputType = a.getString(R.styleable.vericationCodeInput_inputType);
        boxWidth = (int) a.getDimension(R.styleable.vericationCodeInput_child_width, boxWidth);
        boxHeight = (int) a.getDimension(R.styleable.vericationCodeInput_child_height, boxHeight);
        textSize = a.getDimensionPixelSize(R.styleable.vericationCodeInput_textSize, 15);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        initViews();
    }

    private void initViews() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged:", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged:", s.toString() + " start:" + start + " before:" + before + " count:" + count);
                if (s.length() > 1) {
                    for (int i = box - 1; i >= 0; i--) {
                        EditText editText = (EditText) getChildAt(i);
                        if (editText.isFocused() && editText.getText().length() != 0) {
                            editText.setText(String.valueOf(s.charAt(start)));
                            break;
                        }
                    }
                }
                checkAndCommit();
                if (s.length() != 0) {
                    focus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        };


        OnKeyListener onKeyListener = new OnKeyListener() {
            @Override
            public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    backFocus();
                }
                return false;
            }
        };

        for (int i = 0; i < box; i++) {
            final EditText editText = new EditText(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;
            editText.setOnKeyListener(onKeyListener);
            setBg(editText, false);
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    setBg(editText, hasFocus);
                }
            });
            editText.setTextColor(Color.BLACK);
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            if (TYPE_NUMBER.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD.equals(inputType)) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            editText.setId(i);
            editText.setEms(1);
            editText.addTextChangedListener(textWatcher);
            if (i == 0) {
                editText.setFocusable(true);
                editText.requestFocusFromTouch();
            }
            addView(editText, i);
        }


    }

    private void backFocus() {
        int count = getChildCount();
        EditText editText;
        for (int i = count - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                editText.setSelection(1);
                return;
            }
        }
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    private void setBg(EditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < box; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        Log.d(TAG, "checkAndCommit:" + stringBuilder.toString());
        if (listener != null) {
            listener.onTextChange(stringBuilder.toString());
            if (stringBuilder.length() == box) {
                listener.onTextComplete(stringBuilder.toString());
            }
        }
        if (stringBuilder.length() == box) {
            setEnabled(false);
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setOnTextChangeListener(Listener listener) {
        this.listener = listener;
    }

    @Override

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    public interface Listener {
        void onTextChange(String content);

        void onTextComplete(String content);
    }

    public void clear() {
        setEnabled(true);
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View view = getChildAt(i);
            if (view instanceof EditText) {
                if (i == 0) {
                    view.setFocusable(true);
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                }
                ((EditText) view).setText("");
            }
        }
    }

}