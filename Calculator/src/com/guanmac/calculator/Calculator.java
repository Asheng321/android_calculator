package com.guanmac.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Calculator extends Activity
{
	private static final int CMD_CLEAR_HISTORY = 1;
	private static final int CMD_BASIC_PANEL = 2;
	private static final int CMD_ADVANCED_PANEL = 3;

	public static final int BASIC_PANEL = 0;
	public static final int ADVANCED_PANEL = 1;

	private static final String LOG_TAG = "Calculator";
	private static final boolean DEBUG = false;
	private static final boolean LOG_ENABLED = DEBUG ? Config.LOGD
			: Config.LOGV;
	private static final String STATE_CURRENT_VIEW = "state-current-view";

	EventListener mListener = new EventListener();
	// HVGA的分辨率
	private static final int HVGA_WIDTH_PIXELS = 320;
	private CalculatorDisplay mDisplay;
	private History mHistory;
	private Logic mLogic;
	private PanelSwitcher mPanelSwitcher;
	private Persist mPersist;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPersist = new Persist(this);
		mHistory = mPersist.history;
		HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory,
				mLogic);
		mHistory.setObserver(historyAdapter);

		mDisplay = (CalculatorDisplay) this.findViewById(R.id.display);
		mLogic = new Logic(this, mHistory, mDisplay,
				(Button) findViewById(R.id.equal));

		mPanelSwitcher = (PanelSwitcher) findViewById(R.id.panelswitch);
		mPanelSwitcher.setCurrentIndex(savedInstanceState == null ? 0
				: savedInstanceState.getInt(STATE_CURRENT_VIEW));

		mListener.setHnadler(mLogic, mPanelSwitcher);

		mDisplay.setOnKeyListener(mListener);

		View view;
		if ((view = findViewById(R.id.del)) != null)
		{
			view.setOnLongClickListener(mListener);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);

		super.onCreateOptionsMenu(menu);
		MenuItem item;
		item = menu.add(0, CMD_CLEAR_HISTORY, 0, R.string.clear_history);
		item.setIcon(R.drawable.clear_history);

		item = menu.add(0, CMD_ADVANCED_PANEL, 0, R.string.advanced);
		item.setIcon(R.drawable.advanced);

		item = menu.add(0, CMD_BASIC_PANEL, 0, R.string.basic);
		item.setIcon(R.drawable.simple);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		menu.findItem(CMD_BASIC_PANEL).setVisible(
				mPanelSwitcher != null
						&& mPanelSwitcher.getCurrentIndex() == ADVANCED_PANEL);
		menu.findItem(CMD_ADVANCED_PANEL).setVisible(
				mPanelSwitcher != null
						&& mPanelSwitcher.getCurrentIndex() == BASIC_PANEL);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case CMD_CLEAR_HISTORY:
			mHistory.clear();
			break;
		case CMD_BASIC_PANEL:
			if (mPanelSwitcher != null
					&& mPanelSwitcher.getCurrentIndex() == ADVANCED_PANEL)
			{
				mPanelSwitcher.moveRight();
			}
			break;
		case CMD_ADVANCED_PANEL:
			if (mPanelSwitcher != null
					&& mPanelSwitcher.getCurrentIndex() == BASIC_PANEL)
			{
				mPanelSwitcher.moveLeft();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 把当前页面序号存储起来，用于程序中断
	 */
	@Override
	protected void onSaveInstanceState(Bundle state)
	{
		super.onSaveInstanceState(state);
		state.putInt(STATE_CURRENT_VIEW, mPanelSwitcher.getCurrentIndex());
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mLogic.updateHistory();
		mPersist.save();
	}

	static void log(String message)
	{
		if (LOG_ENABLED)
		{
			Log.v(LOG_TAG, message);
		}
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
