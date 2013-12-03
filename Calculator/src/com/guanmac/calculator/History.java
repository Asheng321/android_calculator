package com.guanmac.calculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import android.widget.BaseAdapter;

/**
 * History用到了观察者模式，以及适配器
 * 
 * @author Administrator
 * 
 */
public class History
{
	// 这个版本号对应着Persist类中的版本号。
	private static final int VERSION_1 = 1;
	// 定义历史记录的最大数
	private static final int MAX_ENTRIES = 100;
	// 使用Vector来存储历史记录Entry,我觉得这是不对的，
	// 虽然Vector是线程安全，但是相对于List来说，他的效率有点低 。
	Vector<HistoryEntry> mEntries = new Vector<HistoryEntry>();

	// 用于记录当前记录位置
	int mPos;
	// 适配器
	BaseAdapter mObserver;

	/**
	 * 首次初始化时，先进行清空
	 */
	public History()
	{
		clear();
	}

	/**
	 * 
	 * @param version
	 *            版本号
	 * @param in
	 *            从Persist中传入来的历史记录
	 * @throws IOException
	 */
	public History(int version, DataInputStream in) throws IOException
	{
		// TODO Auto-generated constructor stub
		if (version >= VERSION_1)
		{
			int size = in.readInt();
			for (int i = 0; i < size; i++)
			{
				mEntries.add(new HistoryEntry(version, in));
			}
			mPos = in.readInt();
		}
		else
		{
			throw new IOException("invalid version " + version);
		}
	}

	/**
	 * 设置观察者
	 * 
	 * @param observer
	 */
	public void setObserver(BaseAdapter observer)
	{
		mObserver = observer;
	}

	/**
	 * 设置新的历史记录，记录变更后需要通知适配器页面做出改变
	 */
	void clear()
	{
		// TODO Auto-generated method stub
		mEntries.clear();
		// 尚未知为何要有一个HistoryEntry在里面
		mEntries.add(new HistoryEntry(""));
		mPos = 0;
		notifyChanged();
	}

	/**
	 * 通知更新 适配器要通知页面，记录已改变需要更新 使用notifyDataSetInvalidated()是因为这个方法通知观察器数据更新了，
	 * 只重绘当前可见区域
	 */
	private void notifyChanged()
	{
		// TODO Auto-generated method stub
		if (mObserver != null)
		{

			mObserver.notifyDataSetInvalidated();
		}
	}

	/**
	 * 把历史记录（Vector）写入out中，让其在Persist类中存储。
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void write(DataOutputStream out) throws IOException
	{
		// TODO Auto-generated method stub
		// 先写入版本号（即多少条记录）
		out.writeInt(mEntries.size());
		// 把每条记录中的mBase都写入dataoutputStream
		for (HistoryEntry entry : mEntries)
		{
			entry.write(out);
		}
		// 这个不懂为什么这样做
		out.writeInt(mPos);
	}

	/**
	 * 返回当前vector中当前选中记录
	 * 
	 * @return
	 */
	public HistoryEntry current()
	{
		return mEntries.elementAt(mPos);
	}

	/**
	 * 获得表达式
	 * 
	 * @return
	 */
	public String getText()
	{
		return current().getEdited();
	}

	public String getBase()
	{
		return current().getBase();
	}

	public void update(String text)
	{
		// TODO Auto-generated method stub
		current().setEdited(text);
	}

	public void enter(String text)
	{
		// TODO Auto-generated method stub

	}
}
