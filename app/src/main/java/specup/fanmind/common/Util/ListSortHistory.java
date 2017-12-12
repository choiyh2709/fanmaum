package specup.fanmind.common.Util;

import java.util.Comparator;

import specup.fanmind.vo.HistoryList;


public class ListSortHistory implements Comparator<HistoryList>{

	@Override
	public int compare(HistoryList one, HistoryList another) {
		// TODO Auto-generated method stub
		return one.getStarName().compareTo(another.getStarName());
	}
}
