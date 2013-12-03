package com.guanmac.calculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class PanelSwitcher extends FrameLayout
{

	private static final int MAJOR_MOVE = 60;
	private static final int ANIM_DURATION = 400;
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	// 添加手势
	private GestureDetector mGestureDetector;
	// 子视图
	private View mChildren[] = new View[0];

	private int mWidth;
	// 页面转跳时的动作
	private TranslateAnimation inLeft;
	private TranslateAnimation outLeft;
	private TranslateAnimation inRight;
	private TranslateAnimation outRight;

	// 当前视图序号
	private int mCurrentView;
	// on关于记录前一个移动
	private int mPreviousMove;

	/**
	 * 初始化，实现手势的初始化
	 * 
	 * @param context
	 * @param attrs
	 */
	public PanelSwitcher(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mCurrentView = 0;
		// 初始化手势
		mGestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener()
				{
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY)
					{
						// 不接受滑动，如果太短
						int dx = (int) (e2.getX() - e1.getX());

						if (Math.abs(dx) > MAJOR_MOVE
								&& Math.abs(velocityX) > Math.abs(velocityY))
						{
							if (velocityX > 0)
							{
								moveRight();
							}
							else
							{
								moveLeft();
							}
							return true;
						}
						else
						{
							return false;
						}
					}
				});
	}

	/**
	 * 设置当前视图
	 * 
	 * @param current
	 */
	public void setCurrentIndex(int current)
	{
		mCurrentView = current;
		updateCurrentView();
	}

	/**
	 * 更新视图，使当前视图可见，其他不可见
	 */
	private void updateCurrentView()
	{
		for (int i = mChildren.length - 1; i >= 0; --i)
		{
			mChildren[i].setVisibility(i == mCurrentView ? View.VISIBLE
					: View.GONE);
		}
	}

	/**
	 * 使当前视图不可见，让下一个视图可见
	 */
	public void moveLeft()
	{
		// 如果当前视图序号小于总视图数，而，上一个动作不是向左，就执行
		if (mCurrentView < mChildren.length - 1 && mPreviousMove != LEFT)
		{
			mChildren[mCurrentView + 1].setVisibility(View.VISIBLE);
			mChildren[mCurrentView + 1].startAnimation(inLeft);
			mChildren[mCurrentView].startAnimation(outLeft);
			mChildren[mCurrentView].setVisibility(View.GONE);

			mCurrentView++;
			mPreviousMove = LEFT;
		}
	}

	public void moveRight()
	{
		if (mCurrentView > 0 && mPreviousMove != RIGHT)
		{
			mChildren[mCurrentView - 1].setVisibility(View.VISIBLE);
			mChildren[mCurrentView - 1].startAnimation(inRight);
			mChildren[mCurrentView].startAnimation(outRight);
			mChildren[mCurrentView].setVisibility(View.GONE);

			mCurrentView--;
			mPreviousMove = RIGHT;
		}
	}

	/**
	 * 返回当前页面序号
	 * 
	 * @return
	 */
	public int getCurrentIndex()
	{
		return mCurrentView;
	}

	/**
	 * 初始化mChildren，把页面放入数组
	 */
	@Override
	protected void onFinishInflate()
	{
		int count = this.getChildCount();
		mChildren = new View[count];
		for (int i = 0; i < count; ++i)
		{
			mChildren[i] = getChildAt(i);
		}
		this.updateCurrentView();
	}

	/**
	 * 初始化TranslateAnimation
	 * 
	 * @param w
	 * @param h
	 * @param oldW
	 * @param oldH
	 */
	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH)
	{
		mWidth = w;
		inLeft = new TranslateAnimation(mWidth, 0, 0, 0);
		outLeft = new TranslateAnimation(0, -mWidth, 0, 0);
		inRight = new TranslateAnimation(-mWidth, 0, 0, 0);
		outRight = new TranslateAnimation(0, mWidth, 0, 0);

		inLeft.setDuration(ANIM_DURATION);
		outLeft.setDuration(ANIM_DURATION);
		inRight.setDuration(ANIM_DURATION);
		outRight.setDuration(ANIM_DURATION);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mGestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		return mGestureDetector.onTouchEvent(event);
	}
}
