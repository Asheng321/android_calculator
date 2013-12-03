package com.guanmac.calculator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;

/**
 * �洢��ʷ��¼ ʹ���ļ�calculator.data��¼�� ʹ�ð汾�ſ���
 * 
 * @author Administrator
 * 
 */
public class Persist
{
	private static final int LAST_VERSION = 1;
	private static final String FILE_NAME = "calculator.data";
	private Context mContext;

	History history = new History();

	/**
	 * �ڹ����ʱ����ж�ȡ�ļ�������ʷ��¼
	 * 
	 * @param context
	 */
	Persist(Context context)
	{
		this.mContext = context;
		load();
	}

	/**
	 * �����ļ��е���ʷ��¼(dataInputStream)����ȥHistory�С�
	 * history��ʵ�������dataInputStream�����ݷ���һ��Vector��
	 */
	private void load()
	{
		try
		{
			InputStream is = new BufferedInputStream(
					mContext.openFileInput(FILE_NAME), 8192);
			DataInputStream in = new DataInputStream(is);
			int version = in.readInt();
			if (version > this.LAST_VERSION)
			{
				throw new IOException("data version" + version + ");expected"
						+ LAST_VERSION);
			}
			history = new History(version, in);
			in.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ����ʷ��¼�洢���ļ��� ͨ��History��write�������Ѽ�¼д��out�С�
	 */
	void save()
	{
		try
		{
			OutputStream os = new BufferedOutputStream(mContext.openFileOutput(
					FILE_NAME, 0), 8192);
			DataOutputStream out = new DataOutputStream(os);
			out.writeInt(LAST_VERSION);
			history.write(out);
			out.close();
		}
		catch (IOException e)
		{

		}
	}
}
