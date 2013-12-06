package com.guanmac.calculator;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

/**
 * ��ʷ��¼DOM
 * 
 * @author Administrator
 * 
 */
public class HistoryEntry
{
	// ��¼�汾��
	private static final int VERSION_1 = 1;
	// ���ڼ�¼��������
	private String mBase;
	private String mEdited;

	/**
	 * ����clearEdited()������mBase��mEdited����Ϊ�������
	 */
	public HistoryEntry(String string)
	{
		// TODO Auto-generated constructor stub
		mBase = string;
		clearEdited();
	}

	/**
	 * ������İ汾�Ŵ��ھ�̬�汾�ţ��Ͷ���in�е����룬�����׳�����
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
	 * �ú����ڳ�ʼ�������е��á� ��base�����ݸ��Ƹ�mEdited
	 */
	public void clearEdited()
	{
		mEdited = mBase;
	}

	/**
	 * �������historyд������ʱ���õģ� history�е�write������Persist���ñ���ʱ��
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
