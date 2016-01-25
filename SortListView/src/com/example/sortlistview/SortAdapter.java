package com.example.sortlistview;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private String lastLetter = "";
	private String thisLetter = "";

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * ��ListView���ݷ����仯ʱ,���ô˷���������ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.lineBetween = (FrameLayout) view
					.findViewById(R.id.lineBetween);
			viewHolder.lineBottom = (FrameLayout) view
					.findViewById(R.id.lineBottom);
			viewHolder.lineTop = (FrameLayout) view.findViewById(R.id.lineTop);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// ����position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);


//		if (!thisLetter.equals("A") && arg2.getChildAt(position - 1) != null) {
//			if (!thisLetter.equals(lastLetter)) {
//				ViewHolder v = (ViewHolder) arg2.getChildAt(position - 1)
//						.getTag();
//				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//						v.lineBottom.getLayoutParams());
//				lp.setMargins(0, 0, 0, 0);
//				v.lineBottom.setLayoutParams(lp);
//			} else {
//				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//						viewHolder.lineBottom.getLayoutParams());
//				lp.setMargins(30, 0, 0, 0);
//				viewHolder.lineBottom.setLayoutParams(lp);
//			}
//		}
		// �����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
			viewHolder.lineBetween.setVisibility(View.VISIBLE);
			viewHolder.lineTop.setVisibility(View.VISIBLE);
			viewHolder.lineBottom.setVisibility(View.GONE);
//			lastLetter = thisLetter;
//			thisLetter = mContent.getSortLetters();
//			if (lastLetter.equals(""))
//				lastLetter = mContent.getSortLetters();
		} else {
			viewHolder.lineBottom.setVisibility(View.VISIBLE);
			viewHolder.lineTop.setVisibility(View.GONE);//red
			viewHolder.tvLetter.setVisibility(View.GONE);
			viewHolder.lineBetween.setVisibility(View.GONE);
		}

		viewHolder.tvTitle.setText(this.list.get(position).getName());

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		FrameLayout lineBetween, lineBottom, lineTop;
	}

	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}