package com.example.sortlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;

public class MainActivity extends Activity {
	private ListView sortListView;
	private SortAdapter adapter;
	private SideBar sideBar;
	private TextView dialog;
	private ClearEditText mClearEditText;
	boolean isFirst = true;

	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;
	// pinnerʵ��
	private LinearLayout titleLayout;
	private int lastFirstVisibleItem = -1;
	private TextView title;
	private boolean isFilterMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		title = (TextView) findViewById(R.id.title);
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		String[] arr = getResources().getStringArray(R.array.date);
		addFirstWord(arr);
		SourceDateList = filledData(arr);
		// SideBar.show = setHide();
		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		sideBar.setTextView(dialog);
		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				Toast.makeText(getApplication(),
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});
		sortListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			/**
			 * AbsListView����ʱִ��
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// Log.d("onScroll", "firstVisibleItem" + firstVisibleItem
				// + "visibleItemCount" + visibleItemCount);
				if (visibleItemCount != 0) {
					title.setVisibility(isFilterMode ? View.INVISIBLE
							: View.VISIBLE);
					SortModel modelSecond = new SortModel();
					SortModel modelFirst = (SortModel) adapter
							.getItem(firstVisibleItem);
					if (adapter.getCount() > firstVisibleItem + 1)
						modelSecond = (SortModel) adapter
								.getItem(firstVisibleItem + 1);

					if (firstVisibleItem != lastFirstVisibleItem) {
						MarginLayoutParams params = (MarginLayoutParams) titleLayout
								.getLayoutParams();
						params.topMargin = 0;
						titleLayout.setLayoutParams(params);
						if (modelFirst.getSortLetters() != null) {
							title.setText(modelFirst.getSortLetters());
						}
					}

					if (modelFirst.isGroup() == false
							&& modelSecond.isGroup() == true) {
						View childView = view.getChildAt(1);
						if (childView != null) {
							int titleHeight = titleLayout.getHeight();

							int top = childView.getTop();
							MarginLayoutParams params = (MarginLayoutParams) titleLayout
									.getLayoutParams();
							if (top <= titleHeight) {
								float pushedDistance = -(titleHeight - top);
								params.topMargin = (int) pushedDistance;
								titleLayout.setLayoutParams(params);
							}
						}
					}
					lastFirstVisibleItem = firstVisibleItem;
				} else {
					title.setVisibility(View.INVISIBLE);
					if (!isFirst) {
						mClearEditText.setShakeAnimation(2, 500);
					}
				}
			}
		});

		// �������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// ������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				isFilterMode = s.length() != 0;
				adapter.isFilterMode = isFilterMode;
				Log.d("length", s.length() + "visible" + title.getVisibility());
				filterData(s.toString());
				if (isFirst)
					isFirst = false;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mClearEditText.requestFocus();
	}

	// private ArrayList<String> setHide() {
	// ArrayList<String> temp = new ArrayList<String>();
	// for (int i = 0; i < SourceDateList.size(); i++) {
	// if (!temp.contains(SourceDateList.get(i).getSortLetters())) {
	// temp.add(SourceDateList.get(i).getSortLetters());
	// }
	// }
	// return temp;
	// }

	private void addFirstWord(String[] arr) {

	}

	/**
	 * ΪListView�������
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			if (date[i].matches("[A-Z]")) {
				sortModel.setSortLetters(date[i]);
				sortModel.setGroup(true);
			} else {
				// ����ת����ƴ��
				String pinyin = characterParser.getSelling(date[i]);
				String sortString = pinyin.substring(0, 1).toUpperCase();

				// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}
				sortModel.setGroup(false);
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
