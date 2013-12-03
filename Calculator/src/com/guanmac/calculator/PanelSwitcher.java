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
	// �������
	private GestureDetector mGestureDetector;
	// ����ͼ
	private View mChildren[] = new View[0];

	private int mWidth;
	// ҳ��ת��ʱ�Ķ���
	private TranslateAnimation inLeft;
	private TranslateAnimation outLeft;
	private TranslateAnimation inRight;
	private TranslateAnimation outRight;

	// ��ǰ��ͼ���
	private int mCurrentView;
	// on���ڼ�¼ǰһ���ƶ�
	private int mPreviousMove;

	/**
	 * ��ʼ����ʵ�����Ƶĳ�ʼ��
	 * 
	 * @param context
	 * @param attrs
	 */
	public PanelSwitcher(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mCurrentView = 0;
		// ��ʼ������
		mGestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener()
				{
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY)
					{
						// �����ܻ��������̫��
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
	 * ���õ�ǰ��ͼ
	 * 
	 * @param current
	 */
	public void setCurrentIndex(int current)
	{
		mCurrentView = current;
		updateCurrentView();
	}

	/**
	 * ������ͼ��ʹ��ǰ��ͼ�ɼ����������ɼ�
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
	 * ʹ��ǰ��ͼ���ɼ�������һ����ͼ�ɼ�
	 */
	public void moveLeft()
	{
		// �����ǰ��ͼ���С������ͼ����������һ�������������󣬾�ִ��
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
	 * ���ص�ǰҳ�����
	 * 
	 * @return
	 */
	public int getCurrentIndex()
	{
		return mCurrentView;
	}

	/**
	 * ��ʼ��mChildren����ҳ���������
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
	 * ��ʼ��TranslateAnimation
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
