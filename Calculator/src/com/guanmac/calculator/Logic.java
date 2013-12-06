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

	private static final int ROUND_DIGITS = 1;

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
	 * @throws SyntaxException
	 */
	public String evaluate(String input) throws SyntaxException
	{

		// ��inputΪ��ʱ�����ؿ�
		if (input.trim().equals(""))
			return "";
		int size = input.length();
		// ֱ�����ʽ���һ���ַ����ǲ�������
		// �ѱ��ʽ����input,����input�ͳ��˲���=�ı��ʽ
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
	public static boolean isOperator(char c)
	{
		// plus minus times div
		return "+\u2212\u00d7\u00f7/*".indexOf(c) != -1;
	}

	/**
	 * ����ǲ��ǿɽ��ܵ����� ��������ݲ�����delta;delta���ǲ��������������λ�ò�����������ݵĳ��ȡ�
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
	 * �����ʾ�������
	 */
	private String getText()
	{
		return mDisplay.getText().toString();
	}

	/**
	 * ������ʷ��¼������History�ķ���
	 */
	void updateHistory()
	{
		// TODO Auto-generated method stub
		mHistory.update(getText());
	}

	// ����ʾ���в�������
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

	// ��������ʱ
	public void onEnter()
	{
		// TODO Auto-generated method stub
		String text = getText();
		if (text.equals(mResult))
		{
			// ������һ�����֮�����
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
				// û����Ҫ��ʾ�Ľ����
				clearWithHistory(true);
			}
			else
			{
				setText(mResult);
			}
		}
	}

	/**
	 * ������ʾ������
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
	 * ���
	 */
	public void cleared()
	{
		// TODO Auto-generated method stub
		mResult = "";
		mIsError = false;
		updateHistory();
	}

	/**
	 * �����ʾ������
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
