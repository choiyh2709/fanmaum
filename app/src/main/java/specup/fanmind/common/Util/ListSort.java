package specup.fanmind.common.Util;

import java.util.Comparator;

import specup.fanmind.vo.SupportList;


public class ListSort implements Comparator<SupportList>{

	@Override
	public int compare(SupportList one, SupportList another) {
		// TODO Auto-generated method stub
		return one.getStarName().compareTo(another.getStarName());
	}
}
