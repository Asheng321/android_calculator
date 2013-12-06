package com.guanmac.calculator;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 历史记录DOM
 * 
 * @author Administrator
 * 
 */
public class HistoryEntry
{
	// 记录版本号
	private static final int VERSION_1 = 1;
	// 用于记录整条方程
	private String mBase;
	private String mEdited;

	/**
	 * 调用clearEdited()方法把mBase和mEdited复制为传入参数
	 */
	public HistoryEntry(String string)
	{
		// TODO Auto-generated constructor stub
		mBase = string;
		clearEdited();
	}

	/**
	 * 当传入的版本号大于静态版本号，就读入in中的输入，否则抛出错误
	 * 
	 */
	public HistoryEntry(int version, DataInputStream in) throws IOException
	{
		// TODO Auto-generated constructor stub
		if (version >= VERSION_1)
		{
			mBase = in.readUTF();
			mEdited = in.readUTF();
		}
		else
		{
			throw new IOException("invalid version " + version);
		}
	}

	/**
	 * 该函数在初始化函数中调用。 把base的内容复制给mEdited
	 */
	public void clearEdited()
	{
		mEdited = mBase;
	}

	/**
	 * 这个是在history写入数据时调用的， history中的write则是有Persist调用保存时。
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void write(DataOutput out) throws IOException
	{
		out.writeUTF(mBase);
		out.writeUTF(mEdited);
	}

	@Override
	public String toString()
	{
		return mBase;
	}

	public String getBase()
	{
		return mBase;
	}

	public void setBase(String Base)
	{
		this.mBase = Base;
	}

	public String getEdited()
	{
		return mEdited;
	}

	public void setEdited(String Edited)
	{
		this.mEdited = Edited;
	}
}
