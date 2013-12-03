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
 * 存储历史记录 使用文件calculator.data记录。 使用版本号控制
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
	 * 在构造的时候进行读取文件加载历史记录
	 * 
	 * @param context
	 */
	Persist(Context context)
	{
		this.mContext = context;
		load();
	}

	/**
	 * 加载文件中的历史记录(dataInputStream)，放去History中。
	 * history的实例化会把dataInputStream的数据放入一个Vector中
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
	 * 把历史记录存储到文件中 通过History的write方法，把记录写入out中。
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
