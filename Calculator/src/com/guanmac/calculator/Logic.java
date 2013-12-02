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

	// �Ƿ����
	private boolean mIsError = false;
	// �ú�ĳ���
	private int mLineLength = 0;
	// �������룬����ʲô��˼
	private static final String INFINITY_UNICODE = "\u221e";
	// �����
	private static final String INFINITY = "Infinity";
	// �������� no a Number
	private static final String NAN = "NaN";
	// Ŀ���Ǽ��ţ����Ǻܶ�
	static final char MINUS = '\u2212';
	// �洢������Ϣ
	private final String mErrorString;

	/**
	 * Ϊ�λ��и�button; ���캯��
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
	 * ɾ����ʷ��Ϣ
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
	 * ���ݱ��ʽ�������� ��HistoryAdapter�м�����ʷ��¼���ʱ�õ�
	 * 
	 * @param input
	 * @return
	 */
	public String evaluate(String input)
	{
		// ��inputΪ��ʱ�����ؿ�
		if (input.trim().equals(""))
			return "";
		int size = input.length();
		//ֱ�����ʽ���һ���ַ����ǲ�������
		//�ѱ��ʽ����input,����input�ͳ��˲���=�ı��ʽ
		while(size>0 && isOperator(input.charAt(size-1)))
		{
			input = input.substring(0, size-1);
			--size;
		}
		String result = Util.doubleToString(arg0, arg1)
		return null;
	}

	/**
	 * δ��
	 * 
	 * @param nDigits
	 */
	void setLineLength(int nDigits)
	{
		mLineLength = nDigits;
	}

	/**
	 * �ж��Ƿ��ǲ�����
	 */
	public boolean isOperator(String text)
	{
		return text.length() == 1 && isOperator(text.charAt(0));
	}

	/**
	 * �ж��Ƿ��ǲ�����
	 */
	public boolean isOperator(char c)
	{
		// plus minus times div
		return "+\u2212\u00d7\u00f7/*".indexOf(c) != -1;
	}
}
