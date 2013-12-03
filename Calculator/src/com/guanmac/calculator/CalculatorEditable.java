package com.guanmac.calculator;

import android.text.Editable;
import android.text.SpannableStringBuilder;

/**
 * �Զ���SpannablesStringBuilder��ʵ�ָ��ı�
 * 
 * @author Administrator
 * 
 */
public class CalculatorEditable extends SpannableStringBuilder
{
	// ����
	private static final char[] ORIGINALS = { '-', '*', '/' };
	private static final char[] REPLACEMENS = { '\u2212', '\u00d7', '\u00f7' };

	// δ֪
	private boolean isInsideReplace = false;
	private Logic mlogic;

	/**
	 * ��ʼ������������Factory
	 * 
	 * @param source
	 * @param logic
	 */
	public CalculatorEditable(CharSequence source, Logic logic)
	{
		// TODO Auto-generated constructor stub
		super(source);
		this.mlogic = logic;

	}

	/**
	 * Replaces the specified range (st��en) of text in this Editable with a copy
	 * of the slice start��end from source. The destination slice may be empty,
	 * in which case the operation is an insertion, or the source slice may be
	 * empty, in which case the operation is a deletion.
	 * 
	 * Before the change is committed, each filter that was set with
	 * setFilters(InputFilter[]) is given the opportunity to modify the source
	 * text.
	 * 
	 * If source is Spanned, the spans from it are preserved into the Editable.
	 * Existing spans within the Editable that entirely cover the replaced range
	 * are retained, but any that were strictly within the range that was
	 * replaced are removed. As a special case, the cursor position is preserved
	 * even when the entire range where it is located is replaced. δ֪
	 */
	@Override
	public SpannableStringBuilder replace(int start, int end, CharSequence tb,
			int tbstart, int tbend)
	{
		if (isInsideReplace)
		{
			return super.replace(start, end, tb, tbstart, tbend);
		}
		else
		{
			isInsideReplace = true;
			try
			{
				String delta = tb.subSequence(tbstart, tbend).toString();
				return internalReplace(start, end, delta);
			}
			finally
			{
				isInsideReplace = false;
			}
		}
	}

	/**
	 * δ��֮��
	 */

	private SpannableStringBuilder internalReplace(int start, int end,
			String delta)
	{
		// TODO Auto-generated method stub
		if (!mlogic.acceptInsert(delta))
		{
			mlogic.cleared();
			start = 0;
			end = length();
		}
		for (int i = ORIGINALS.length - 1; i >= 0; --i)
		{
			delta = delta.replace(ORIGINALS[i], REPLACEMENS[i]);
		}

		int length = delta.length();
		if (length == 1)
		{
			char text = delta.charAt(0);

			// ��ֹ����.��ͬһ��������
			if (text == '.')
			{
				int p = start - 1;
				while (p >= 0 && Character.isDigit(charAt(p)))
				{
					--p;
				}
				if (p >= 0 && charAt(p) == '.')
				{
					return super.replace(start, end, "");
				}
			}
			char prevChar = start > 0 ? charAt(start - 1) : '\0';
			// ��ֹ�����Ⱥ�
			if (text == Logic.MINUS && prevChar == Logic.MINUS)
			{
				return super.replace(start, end, "");

			}
			// ��ֹ����������
			if (Logic.isOperator(text))
			{
				while (Logic.isOperator(prevChar)
						&& (text != Logic.MINUS || prevChar == '+'))
				{
					return super.replace(start, end, "");
				}
			}
			// dont allow leading operator +*/
			if (start == 0 && Logic.isOperator(text) && text != Logic.MINUS)
			{
				return super.replace(start, end, "");
			}
		}
		return super.replace(start, end, delta);
	}

	/**
	 * �ڲ�Factory�࣬���ڴ����µ�CalculatorEditable �ڲ���̬�����public
	 * 
	 * @author Administrator
	 * 
	 */
	public static class Factory extends Editable.Factory
	{
		private Logic mlogic;

		public Factory(Logic logic)
		{
			this.mlogic = logic;
		}

		@Override
		public Editable newEditable(CharSequence source)
		{
			return new CalculatorEditable(source, mlogic);
		}
	}
}
