package com.guanmac.calculator;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class EventListener implements OnClickListener, OnLongClickListener
{
	Logic mHandler;
	PanelSwitcher mPanelSwitcher;

	// ÉèÖÃHandler
	void setHnadler(Logic handler, PanelSwitcher panelSwitcher)
	{
		this.mHandler = handler;
		this.mPanelSwitcher = panelSwitcher;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id)
		{
		case R.id.del:
			mHandler.onDelete();
			break;
		case R.id.equal:
			mHandler.onEnter();
			break;

		default:
			if (v instanceof Button)
			{
				String text = ((Button) v).getText().toString();
				if (text.length() >= 2)
				{
					text += '(';
				}
				mHandler.insert(text);
				if (mPanelSwitcher != null
						&& mPanelSwitcher.getCurrentIndex() == Calculator.ADVANCED_PANEL)
				{
					mPanelSwitcher.moveRigth();
				}
			}
		}
	}

	@Override
	public boolean onLongClick(View v)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
