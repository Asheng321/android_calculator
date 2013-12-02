package com.guanmac.calculator;

import java.util.Vector;
import org.javia.arity.SyntaxException;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ��ʷ��¼������
 * 
 * @author Administrator
 * 
 */
public class HistoryAdapter extends BaseAdapter
{
	private Vector<HistoryEntry> mEntries;
	// ������ res/layout �µ� xml �����ļ�������ʵ����
	private LayoutInflater mInflater;
	private Logic mEval;

	HistoryAdapter(Context context, History history, Logic evaluator)
	{
		mEntries = history.mEntries;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mEval = evaluator;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		// ��һ ����Ϊ��vector����һ��HistoryEntry�ǿյġ�������ʾ����
		return mEntries.size() - 1;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mEntries.elementAt(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * �Ƿ�ָ��������ͼ��������ͼ��ID��Ӧ�ĺ�̨���ݸı�Ҳ�ᱣ�ָ�ID.
	 */
	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
		// TODO Auto-generated method stub
		if (convertView == null)
		{
			view = mInflater.inflate(R.layout.history_item, parent, false);
		}
		else
		{
			view = convertView;
		}
		// ���ʽ tv
		TextView expr = (TextView) view.findViewById(R.id.historyExpr);
		// ���tv
		TextView result = (TextView) view.findViewById(R.id.historyResult);

		HistoryEntry entry = mEntries.elementAt(position);

		String base = entry.getBase();
		expr.setText(base);
		String re = mEval.evaluate(base);
		result.setText("=" + re);
		return view;
	}
}
