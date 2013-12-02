package com.guanmac.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.widget.TextView;

public class Calculator extends Activity
{
	private static final int HVGA_WIDTH_PIXELS = 320;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * �ڲ����ļ��е������С��ָ��ΪHVGA��ʾ���� ��������Ĵ�С��Ӧ�أ�������ǲ�ͬ����ʾ�������С�
	 */
	public void adjustFontSize(TextView view)
	{
		float fontPixelSize = view.getTextSize();
		Display display = this.getWindowManager().getDefaultDisplay();
		int h = Math.min(display.getWidth(), display.getHeight());
		float ratio = (float) h / HVGA_WIDTH_PIXELS;
		// getTextSize()���ص���px��λ����setTextSize������spΪ��λ
		// ����Ҫ����ת����ָ����λ��
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontPixelSize * ratio);
	}
}
