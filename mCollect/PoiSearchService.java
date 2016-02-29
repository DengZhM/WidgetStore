package com.iue.pocketdoc.common.service;

import java.util.List;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.iue.pocketdoc.global.IUEApplication;

public class PoiSearchService implements OnGetPoiSearchResultListener {

	private PoiSearch mPoiSearch = null;
	private PoiSearchSerivceListener mPoiSearchSerivceListener;
	private String keyWords, city;
//	private int resultSize = 0;
//	private int citySize = 0;
	private boolean isFirst = true;

	private void initPoiSearch() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
	}

	/**
	 * �ϴ�combox���ı����ݣ����������������Ƶ�ַ
	 * 
	 * @param city
	 * @param keyWords
	 */

	public void searchPoi(LatLng lat, String keyWords) {
		this.keyWords = keyWords;
		initPoiSearch();

		PoiNearbySearchOption optionNear = new PoiNearbySearchOption();
		optionNear.keyword(keyWords);
		optionNear.location(lat);
		optionNear.pageNum(0);
		optionNear.pageCapacity(10);
		mPoiSearch.searchNearby(optionNear);

	}

	public void searchPoi(String city) {
		initPoiSearch();
		PoiCitySearchOption optionCity = new PoiCitySearchOption();
		optionCity.keyword(keyWords);
		optionCity.city(city);
		optionCity.pageNum(0);
		optionCity.pageCapacity(10);
		mPoiSearch.searchInCity(optionCity);
	}

	public void setPoiSearchSerivceListener(
			PoiSearchSerivceListener mPoiSearchSerivceListener) {
		this.mPoiSearchSerivceListener = mPoiSearchSerivceListener;
	}

	public interface PoiSearchSerivceListener {
		void poiSearchResult(List<PoiInfo> result);
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// ��Ĭ��ȫ����������Ϊ��
			if (isFirst) {
				isFirst = false;
				searchPoi(IUEApplication.province);
				return;
			} else {
				isFirst = true;
				return;
			}
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
			String strInfo = "��";
		//	citySize = result.getSuggestCityList().size();
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			city = result.getSuggestCityList().get(0).city;
			strInfo += "�ҵ����";
			searchPoi(city);// ���е�һ������������
		} else {
			if (result.getAllPoi().size() != 0) {
				isFirst = true;
			//	resultSize = result.getAllPoi().size();
				mPoiSearchSerivceListener.poiSearchResult(result.getAllPoi());
			}
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		

	}

}
