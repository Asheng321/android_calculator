/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.calculator2;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.graphics.Rect;
import android.graphics.Paint;

/**
 * Provides vertical scrolling for the input/result EditText.
 *提供了垂直滚动的输入结果框
 *ViewSwitcher适用于两个视图带动画的切换。
 *CalculatorDisplay带有两个EditText作为输入数字与结果之间的转换
 */
class CalculatorDisplay extends ViewSwitcher {
    // only these chars are accepted from keyboard
    // 只有以下字符会被接受
    private static final char[] ACCEPTED_CHARS = 
        "0123456789.+-*/\u2212\u00d7\u00f7()!%^".toCharArray();
    // 设置动画时长
    private static final int ANIM_DURATION = 500;
    // 滚动代表（上，下，没动作）
    enum Scroll { UP, DOWN, NONE }
    // 透明度渐变动画
    TranslateAnimation inAnimUp;
    TranslateAnimation outAnimUp;
    TranslateAnimation inAnimDown;
    TranslateAnimation outAnimDown;

    private Logic mLogic;
    private boolean mComputedLineLength = false;
    
    public CalculatorDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Calculator calc = (Calculator) getContext();
        calc.adjustFontSize((TextView)getChildAt(0));
        calc.adjustFontSize((TextView)getChildAt(1));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mComputedLineLength) {
            mLogic.setLineLength(getNumberFittingDigits((TextView) getCurrentView()));
            mComputedLineLength = true;
        }
    }

    // compute the maximum number of digits that fit in the
    // calculator display without scrolling.
    // 计算出适合的计算器显示屏不滚动的最大位数。
    private int getNumberFittingDigits(TextView display) {
        int available = display.getWidth()
            - display.getTotalPaddingLeft() - display.getTotalPaddingRight();
        Paint paint = display.getPaint();
        float digitWidth = paint.measureText("2222222222") / 10f;
        return (int) (available / digitWidth);
    }

    /**
    *设置逻辑
    */
    protected void setLogic(Logic logic) {
        mLogic = logic;
        //设置ediText接受的字符
        NumberKeyListener calculatorKeyListener =
            new NumberKeyListener() {
                public int getInputType() {
                    // Don't display soft keyboard.
                    // 不显示键盘
                    return InputType.TYPE_NULL;
                }
                // 返回可接受的字符
                protected char[] getAcceptedChars() {
                    return ACCEPTED_CHARS;
                }

                public CharSequence filter(CharSequence source, int start, int end,
                                           Spanned dest, int dstart, int dend) {
                    /* the EditText should still accept letters (eg. 'sin')
                       coming from the on-screen touch buttons, so don't filter anything.
                    */
                    return null;
                }
            };

        Editable.Factory factory = new CalculatorEditable.Factory(logic);
        for (int i = 0; i < 2; ++i) {
            EditText text = (EditText) getChildAt(i);
            text.setBackgroundDrawable(null);
            text.setEditableFactory(factory);
            text.setKeyListener(calculatorKeyListener);
        }
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        getChildAt(0).setOnKeyListener(l);
        getChildAt(1).setOnKeyListener(l);
    }

    //在尺寸改变的时候进行透明度动画初始化 
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        inAnimUp = new TranslateAnimation(0, 0, h, 0);
        inAnimUp.setDuration(ANIM_DURATION);
        outAnimUp = new TranslateAnimation(0, 0, 0, -h);
        outAnimUp.setDuration(ANIM_DURATION);

        inAnimDown = new TranslateAnimation(0, 0, -h, 0);
        inAnimDown.setDuration(ANIM_DURATION);
        outAnimDown = new TranslateAnimation(0, 0, 0, h);
        outAnimDown.setDuration(ANIM_DURATION);
    }
    // 对当前edittext插入字符
    void insert(String delta) {
        EditText editor = (EditText) getCurrentView();
        int cursor = editor.getSelectionStart();
        editor.getText().insert(cursor, delta);
    }
    // 获得当前edidText
    EditText getEditText() {
        return (EditText) getCurrentView();
    }
    //返回Editable是因为editable可改变，剩内存 
    Editable getText() {
        EditText text = (EditText) getCurrentView();
        return text.getText();
    }
    
    /**
    *设置结果框显示内容
    *显示内容时，进行editText的切换（显示下一个）以及添加动画。
    */
    void setText(CharSequence text, Scroll dir) {
        if (getText().length() == 0) {
            dir = Scroll.NONE;
        }
        
        if (dir == Scroll.UP) {
            setInAnimation(inAnimUp);
            setOutAnimation(outAnimUp);            
        } else if (dir == Scroll.DOWN) {
            setInAnimation(inAnimDown);
            setOutAnimation(outAnimDown);            
        } else { // Scroll.NONE
            setInAnimation(null);
            setOutAnimation(null);
        }
        // 获得下一个要显示的editText,设置其内容
        EditText editText = (EditText) getNextView();
        editText.setText(text);
        //Calculator.log("selection to " + text.length() + "; " + text);
        // 设置光标位置
        editText.setSelection(text.length());
        // 显示editext
        showNext();
    }
    
    // 设置光标位置
    void setSelection(int i) {
        EditText text = (EditText) getCurrentView();
        text.setSelection(i);
    }
    // 获得选择的开始序号
    int getSelectionStart() {
        EditText text = (EditText) getCurrentView();
        return text.getSelectionStart();
    }
    
    @Override
    protected void onFocusChanged(boolean gain, int direction, Rect prev) {
        //Calculator.log("focus " + gain + "; " + direction + "; " + prev);
        if (!gain) {
            requestFocus();
        }
    }
}
