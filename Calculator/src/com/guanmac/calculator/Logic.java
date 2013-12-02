package com.guanmac.calculator;

import org.javia.arity.Symbols;
import org.javia.arity.Util;
import android.content.Context;
import android.widget.Button;

public class Logic
{

	private CalculatorDisplay mDisplay;
	private Symbols mSymbols = new Symbols();

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
	 */
	public String evaluate(String input)
	{
		// 当input为空时，返回空
		if (input.trim().equals(""))
			return "";
		int size = input.length();
		//直到表达式最后一个字符不是操作符，
		//把表达式赋给input,这是input就成了不带=的表达式
		while(size>0 && isOperator(input.charAt(size-1)))
		{
			input = input.substring(0, size-1);
			--size;
		}
		String result = Util.doubleToString(arg0, arg1)
		return null;
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
	public boolean isOperator(char c)
	{
		// plus minus times div
		return "+\u2212\u00d7\u00f7/*".indexOf(c) != -1;
	}
}
