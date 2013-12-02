package com.guanmac.calculator;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 提供了垂直滚动的输入结果框。ViewSwitcher适用于两个视图带动画的切换。
 * CalculatorDisplay带有两个EditText作为输入数字与结果之间的转换
 * 
 * @author Administrator
 * 
 */
class CalculatorDisplay extends ViewSwitcher
{
	// 只接受以下字符
	private static final char[] ACCEPTED_CHARS = " 0123456789.+-*/\u2212\u00d7\u00f7()!%^"
			.toCharArray();
	// 设置动画时长
	private static final int ANIM_DURATION = 500;

	// 滚动代表（上、下、没动作）
	enum Scroll
	{
		UP, DOWN, NONE
	}

	// 透明度渐变动画
	TranslateAnimation inAnimUp;
	TranslateAnimation outAnimUp;
	TranslateAnimation inAnimDown;
	TranslateAnimation outAnimDown;

	private Logic mLogic;
	// 未解
	private boolean mComputedLineLength = false;

	public CalculatorDisplay(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 当页面完成初始化后，调整整个页面的字体使符合屏幕大小
	 */
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		Calculator calc = (Calculator) getContext();
		calc.adjustFontSize((TextView) this.getChildAt(0));
		calc.adjustFontSize((TextView) this.getChildAt(1));
	}

	/**
	 * 测量每个子视图的长宽
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		if (!mComputedLineLength)
		{
			mLogic.setLineLength(getNumberFittingDigits((TextView) getCurrentView()));
			mComputedLineLength = true;
		}
	}

	/**
	 * 计算出合适的计算器显示屏不滚动的最大位数 理应成为公共函数
	 * 
	 * @param display
	 * @return
	 */
	private int getNumberFittingDigits(TextView display)
	{
		// TODO Auto-generated method stub
		int availble = display.getWidth() - display.getTotalPaddingLeft()
				- display.getTotalPaddingRight();
		Paint paint = display.getPaint();
		float digitWidth = paint.measureText("1111");
		return (int) (availble / digitWidth);
	}

	/**
	 * 设置逻辑，设置editext接受的字符
	 * 
	 * @param logic
	 */
	protected void setLogic(Logic logic)
	{
		this.mLogic = logic;
		NumberKeyListener calculatorKeyListener = new NumberKeyListener()
		{
			// 不显示键盘
			@Override
			public int getInputType()
			{
				// TODO Auto-generated method stub
				return InputType.TYPE_NULL;
			}

			// 返回可接受的字符
			@Override
			protected char[] getAcceptedChars()
			{
				// TODO Auto-generated method stub
				return ACCEPTED_CHARS;
			}
		};
		Editable.Factory factory = new CalculatorEditable.Factory(logic);
		// 尚未懂++i是为何
		for (int i = 0; i < 2; ++i)
		{
			EditText text = (EditText) this.getChildAt(i);
			text.setEditableFactory(factory);
			text.setKeyListener(calculatorKeyListener);
		}
	}

	/**
	 * 设置结果框显示内容 显示内容时，进行editext的切换（显示下一个）以及添加动画
	 */
	public void setText(CharSequence text, Scroll dir)
	{
		if (this.getText().length() == 0)
		{
			dir = Scroll.NONE;
		}
		if (dir == Scroll.UP)
		{
			this.setInAnimation(inAnimUp);
			this.setOutAnimation(outAnimUp);
		}
		else if (dir == Scroll.DOWN)
		{
			this.setInAnimation(inAnimDown);
			this.setInAnimation(outAnimDown);
		}
		else
		{
			this.setInAnimation(null);
			this.setInAnimation(null);
		}
		// 获得下一个要显示的editText，设置其内容
		EditText editText = (EditText) this.getNextView();
		editText.setText(text);
		// 设置光标位置
		editText.setSelection(text.length());
		// 显示editxt
		this.showNext();
	}

	/**
	 * 返回editable是因为editable可编号，省内存
	 */
	public Editable getText()
	{
		EditText text = (EditText) this.getCurrentView();
		return text.getText();
	}
}