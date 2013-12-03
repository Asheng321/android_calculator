package com.guanmac.calculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import android.widget.BaseAdapter;

/**
 * History�õ��˹۲���ģʽ���Լ�������
 * 
 * @author Administrator
 * 
 */
public class History
{
	// ����汾�Ŷ�Ӧ��Persist���еİ汾�š�
	private static final int VERSION_1 = 1;
	// ������ʷ��¼�������
	private static final int MAX_ENTRIES = 100;
	// ʹ��Vector���洢��ʷ��¼Entry,�Ҿ������ǲ��Եģ�
	// ��ȻVector���̰߳�ȫ�����������List��˵������Ч���е�� ��
	Vector<HistoryEntry> mEntries = new Vector<HistoryEntry>();

	// ���ڼ�¼��ǰ��¼λ��
	int mPos;
	// ������
	BaseAdapter mObserver;

	/**
	 * �״γ�ʼ��ʱ���Ƚ������
	 */
	public History()
	{
		clear();
	}

	/**
	 * 
	 * @param version
	 *            �汾��
	 * @param in
	 *            ��Persist�д���������ʷ��¼
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
	 * ���ù۲���
	 * 
	 * @param observer
	 */
	public void setObserver(BaseAdapter observer)
	{
		mObserver = observer;
	}

	/**
	 * �����µ���ʷ��¼����¼�������Ҫ֪ͨ������ҳ�������ı�
	 */
	void clear()
	{
		// TODO Auto-generated method stub
		mEntries.clear();
		// ��δ֪Ϊ��Ҫ��һ��HistoryEntry������
		mEntries.add(new HistoryEntry(""));
		mPos = 0;
		notifyChanged();
	}

	/**
	 * ֪ͨ���� ������Ҫ֪ͨҳ�棬��¼�Ѹı���Ҫ���� ʹ��notifyDataSetInvalidated()����Ϊ�������֪ͨ�۲������ݸ����ˣ�
	 * ֻ�ػ浱ǰ�ɼ�����
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
	 * ����ʷ��¼��Vector��д��out�У�������Persist���д洢��
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void write(DataOutputStream out) throws IOException
	{
		// TODO Auto-generated method stub
		// ��д��汾�ţ�����������¼��
		out.writeInt(mEntries.size());
		// ��ÿ����¼�е�mBase��д��dataoutputStream
		for (HistoryEntry entry : mEntries)
		{
			entry.write(out);
		}
		// �������Ϊʲô������
		out.writeInt(mPos);
	}

	/**
	 * ���ص�ǰvector�е�ǰѡ�м�¼
	 * 
	 * @return
	 */
	public HistoryEntry current()
	{
		return mEntries.elementAt(mPos);
	}

	/**
	 * ��ñ��ʽ
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
