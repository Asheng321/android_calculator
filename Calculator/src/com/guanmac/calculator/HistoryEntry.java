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
	private static final int VERSION_1 = 1;
	// 用于记录整条方程
	private String mBase;
	private String mEdited;

	public HistoryEntry(String string)
	{
		// TODO Auto-generated constructor stub
		mBase = string;
		clearEdited();
	}

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
	 * 未解
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
		this.mBase = mBase;
	}

	public String getEdited()
	{
		return mEdited;
	}

	public void setEdited(String Edited)
	{
		this.mEdited = mEdited;
	}
}
