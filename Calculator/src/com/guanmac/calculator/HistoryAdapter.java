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
 * 历史记录适配器
 * 
 * @author Administrator
 * 
 */
public class HistoryAdapter extends BaseAdapter
{
	private Vector<HistoryEntry> mEntries;
	// 用来找 res/layout 下的 xml 布局文件，并且实例化
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
		// 减一 是因为在vector中有一个HistoryEntry是空的。不用显示出来
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
	 * 是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID.
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
		// 表达式 tv
		TextView expr = (TextView) view.findViewById(R.id.historyExpr);
		// 结果tv
		TextView result = (TextView) view.findViewById(R.id.historyResult);

		HistoryEntry entry = mEntries.elementAt(position);

		String base = entry.getBase();
		expr.setText(base);
		String re = mEval.evaluate(base);
		result.setText("=" + re);
		return view;
	}
}
