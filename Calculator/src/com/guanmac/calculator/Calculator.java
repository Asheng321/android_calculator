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
	 * 在布局文件中的字体大小被指定为HVGA显示屏。 调整字体的大小相应地，如果我们不同的显示器上运行。
	 */
	public void adjustFontSize(TextView view)
	{
		float fontPixelSize = view.getTextSize();
		Display display = this.getWindowManager().getDefaultDisplay();
		int h = Math.min(display.getWidth(), display.getHeight());
		float ratio = (float) h / HVGA_WIDTH_PIXELS;
		// getTextSize()返回的是px单位，而setTextSize则是以sp为单位
		// 所以要进行转化，指定单位。
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontPixelSize * ratio);
	}
}
