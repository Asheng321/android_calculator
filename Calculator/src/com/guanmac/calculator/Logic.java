package com.guanmac.calculator;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;
import org.javia.arity.Util;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Button;

public class Logic
{

	private CalculatorDisplay mDisplay;
	private History mHistory;
	private String mResult = "";

	// 是否出错
	private boolean mIsError = false;
	// 该横的长度
	private int mLineLength = 0;
	// 无穷大编码，不懂什么意思
	private static final String INFINITY_UNICODE = "\u221e";
	// 无穷大
	private static final String INFINITY = "Infinity";
	// 不是数字 no a Number
	private static final String NAN = "NaN";
	// 目测是减号，不是很懂
	static final char MINUS = '\u2212';
	// 存储错误信息
	private final String mErrorString;

	private static final int ROUND_DIGITS = 1;

	/**
	 * 为何会有个button; 构造函数
	 */
	public Logic(Context context, History history, CalculatorDisplay display,
			Button equalButton)
	{
		mErrorString = context.getResources().getString(R.string.error);
		mHistory = history;
		mDisplay = display;
		display.setLogic(this);

		clearWithHistory(false);
	}

	/**
	 * 删除历史信息
	 * 
	 * @param
	 */
	private void clearWithHistory(boolean scroll)
	{
		// TODO Auto-generated method stub
		mDisplay.setText(mHistory.getText(),
				scroll ? CalculatorDisplay.Scroll.UP
						: CalculatorDisplay.Scroll.NONE);
	}

	/**
	 * 根据表达式进行运算 在HistoryAdapter中计算历史记录结果时用到
	 * 
	 * @param input
	 * @return
	 * @throws SyntaxException
	 */
	public String evaluate(String input) throws SyntaxException
	{

		// 当input为空时，返回空
		if (input.trim().equals(""))
			return "";
		int size = input.length();
		// 直到表达式最后一个字符不是操作符，
		// 把表达式赋给input,这是input就成了不带=的表达式
		while (size > 0 && isOperator(input.charAt(size - 1)))
		{
			input = input.substring(0, size - 1);
			--size;
		}
		String result = Util.doubleToString(new Symbols().eval(input),
				mLineLength, ROUND_DIGITS);
		if (result.equals(NAN))
		{
			mIsError = true;
			return mErrorString;
		}
		return result.replace('-', MINUS).replace(INFINITY, INFINITY_UNICODE);
	}

	/**
	 * 未解
	 * 
	 * @param nDigits
	 */
	void setLineLength(int nDigits)
	{
		mLineLength = nDigits;
	}

	/**
	 * 判断是否是操作符
	 */
	public boolean isOperator(String text)
	{
		return text.length() == 1 && isOperator(text.charAt(0));
	}

	/**
	 * 判断是否是操作符
	 */
	public static boolean isOperator(char c)
	{
		// plus minus times div
		return "+\u2212\u00d7\u00f7/*".indexOf(c) != -1;
	}

	/**
	 * 检测是不是可接受的输入 输入框内容不等于delta;delta不是操作符；光标所在位置不是输入框内容的长度。
	 * 
	 * @param delta
	 * @return
	 */
	public boolean acceptInsert(String delta)
	{
		// TODO Auto-generated method stub
		String text = getText();
		return !mIsError
				&& (!mResult.equals(text) || isOperator(delta) || mDisplay
						.getSelectionStart() != text.length());
	}

	/**
	 * 获得显示框的内容
	 */
	private String getText()
	{
		return mDisplay.getText().toString();
	}

	/**
	 * 更新历史记录，调用History的方法
	 */
	void updateHistory()
	{
		// TODO Auto-generated method stub
		mHistory.update(getText());
	}

	// 在显示框中插入数据
	public void insert(String text)
	{
		// TODO Auto-generated method stub
		mDisplay.insert(text);
	}

	public void onDelete()
	{
		// TODO Auto-generated method stub
		if (getText().equals(mResult) || mIsError)
		{
			clear(false);
		}
		else
		{
			mDisplay.dispatchKeyEvent(new KeyEvent(0, KeyEvent.KEYCODE_DEL));
			mResult = "";
		}
	}

	// 输入内容时
	public void onEnter()
	{
		// TODO Auto-generated method stub
		String text = getText();
		if (text.equals(mResult))
		{
			// 在输入一个结果之后清空
			clearWithHistory(false);
		}
		else
		{
			mHistory.enter(text);
			try
			{
				mResult = evaluate(text);
			}
			catch (SyntaxException e)
			{
				mIsError = true;
				mResult = mErrorString;
			}
			if (text.equals(mResult))
			{
				// 没有需要显示的结果，
				clearWithHistory(true);
			}
			else
			{
				setText(mResult);
			}
		}
	}

	/**
	 * 设置显示框内容
	 * 
	 * @param mResult2
	 */
	private void setText(String text)
	{
		// TODO Auto-generated method stub
		mDisplay.setText(text, CalculatorDisplay.Scroll.UP);
	}

	public void onClear()
	{
		// TODO Auto-generated method stub
		clear(false);
	}

	/**
	 * 清空
	 */
	public void cleared()
	{
		// TODO Auto-generated method stub
		mResult = "";
		mIsError = false;
		updateHistory();
	}

	/**
	 * 清空显示框内容
	 * 
	 * @param scroll
	 */
	private void clear(boolean scroll)
	{
		mDisplay.setText("", scroll ? CalculatorDisplay.Scroll.UP
				: CalculatorDisplay.Scroll.NONE);
		cleared();
	}
}
