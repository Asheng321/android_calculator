package com.guanmac.calculator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 自定义按钮，按下有动作
 * 
 * @author Administrator
 * 
 */
public class ColorButton extends Button implements OnClickListener
{
	int CLICK_FEEDBACK_COLOR;
	static final int CLICK_FEEDBACK_INTERVAL = 10;
	static final int CLICK_FEEDBACK_DURATION = 350;
	// 字符串字体大小
	float mTextX;
	float mTextY;
	long mAnimStart;

	OnClickListener mListener;
	Paint mFeedbackPaint;

	public ColorButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		Calculator calc = (Calculator) context;
		init(calc);
		mListener = calc.mListener;
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		mListener.onClick(this);
	}

	/**
	 * 初始化
	 * 
	 * @param calc
	 */
	private void init(Calculator calc)
	{
		Resources res = getResources();
		CLICK_FEEDBACK_COLOR = res.getColor(R.color.magic_flame);

		mFeedbackPaint = new Paint();
		mFeedbackPaint.setStyle(Style.STROKE);
		mFeedbackPaint.setStrokeWidth(2);
		getPaint().setColor(res.getColor(R.color.button_text));

		mAnimStart = -1;
		calc.adjustFontSize(this);
	}

	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH)
	{
		measureText();
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int befor, int after)
	{
		measureText();
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		if (mAnimStart != -1)
		{
			int animDuration = (int) (System.currentTimeMillis() - mAnimStart);
			// 当计算的动画事件大于按钮反馈时间
			if (animDuration >= CLICK_FEEDBACK_DURATION)
			{
				mAnimStart = -1;
			}
			else
			{
				drawMagicFlame(animDuration, canvas);
				// 刷新页面，使用postInvalidateDelayed()在UI主线程中使用
				postInvalidateDelayed(CLICK_FEEDBACK_INTERVAL);
			}

		}
		// 当按下后
		else if (isPressed())
		{
			drawMagicFlame(0, canvas);
		}
		CharSequence text = this.getText();
		canvas.drawText(text, 0, text.length(), mTextX, mTextY, getPaint());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean result = super.onTouchEvent(event);
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:
			animateClickFeedback();
			break;
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_CANCEL:
			invalidate();
			break;
		}
		return result;
	}

	private void animateClickFeedback()
	{
		// TODO Auto-generated method stub
		mAnimStart = System.currentTimeMillis();
		this.invalidate();
	}

	private void drawMagicFlame(int duration, Canvas canvas)
	{
		int alpha = 255 - 255 * duration / CLICK_FEEDBACK_DURATION;
		int color = CLICK_FEEDBACK_COLOR | (alpha << 24);

		mFeedbackPaint.setColor(color);
		canvas.drawRect(1, 1, getWidth() - 1, getHeight() - 1, mFeedbackPaint);
	}

	/**
	 * 初始化mTextX,mTextY
	 */
	private void measureText()
	{
		Paint paint = this.getPaint();
		mTextX = (this.getWidth() - paint.measureText(getText().toString())) / 2;
		// （按钮高度 - 字体上基线到最顶部的距离 - 字体下基线到最底部的距离）/2
		mTextY = (this.getHeight() - paint.ascent() - paint.descent()) / 2;
	}
}
