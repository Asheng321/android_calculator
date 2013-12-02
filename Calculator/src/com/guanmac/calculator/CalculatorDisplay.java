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
 * �ṩ�˴�ֱ��������������ViewSwitcher������������ͼ���������л���
 * CalculatorDisplay��������EditText��Ϊ������������֮���ת��
 * 
 * @author Administrator
 * 
 */
class CalculatorDisplay extends ViewSwitcher
{
	// ֻ���������ַ�
	private static final char[] ACCEPTED_CHARS = " 0123456789.+-*/\u2212\u00d7\u00f7()!%^"
			.toCharArray();
	// ���ö���ʱ��
	private static final int ANIM_DURATION = 500;

	// ���������ϡ��¡�û������
	enum Scroll
	{
		UP, DOWN, NONE
	}

	// ͸���Ƚ��䶯��
	TranslateAnimation inAnimUp;
	TranslateAnimation outAnimUp;
	TranslateAnimation inAnimDown;
	TranslateAnimation outAnimDown;

	private Logic mLogic;
	// δ��
	private boolean mComputedLineLength = false;

	public CalculatorDisplay(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ��ҳ����ɳ�ʼ���󣬵�������ҳ�������ʹ������Ļ��С
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
	 * ����ÿ������ͼ�ĳ���
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
	 * ��������ʵļ�������ʾ�������������λ�� ��Ӧ��Ϊ��������
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
	 * �����߼�������editext���ܵ��ַ�
	 * 
	 * @param logic
	 */
	protected void setLogic(Logic logic)
	{
		this.mLogic = logic;
		NumberKeyListener calculatorKeyListener = new NumberKeyListener()
		{
			// ����ʾ����
			@Override
			public int getInputType()
			{
				// TODO Auto-generated method stub
				return InputType.TYPE_NULL;
			}

			// ���ؿɽ��ܵ��ַ�
			@Override
			protected char[] getAcceptedChars()
			{
				// TODO Auto-generated method stub
				return ACCEPTED_CHARS;
			}
		};
		Editable.Factory factory = new CalculatorEditable.Factory(logic);
		// ��δ��++i��Ϊ��
		for (int i = 0; i < 2; ++i)
		{
			EditText text = (EditText) this.getChildAt(i);
			text.setEditableFactory(factory);
			text.setKeyListener(calculatorKeyListener);
		}
	}

	/**
	 * ���ý������ʾ���� ��ʾ����ʱ������editext���л�����ʾ��һ�����Լ���Ӷ���
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
		// �����һ��Ҫ��ʾ��editText������������
		EditText editText = (EditText) this.getNextView();
		editText.setText(text);
		// ���ù��λ��
		editText.setSelection(text.length());
		// ��ʾeditxt
		this.showNext();
	}

	/**
	 * ����editable����Ϊeditable�ɱ�ţ�ʡ�ڴ�
	 */
	public Editable getText()
	{
		EditText text = (EditText) this.getCurrentView();
		return text.getText();
	}
}