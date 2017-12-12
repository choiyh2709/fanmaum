package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class EventList implements Parcelable {

	String mEventSrl;
	String mTitle;
	String mText;
	String mType;
	String mThum;
	String mBegin;
	String mEnd;
	//이하는 Entring 이벤트만 적용
	String mEntry;
	String mEnterHeart;
	String mWarning;
	String mSupport_srl;
	String star_srl;

	public String getStar_srl() {
		return star_srl;
	}

	public void setStar_srl(String star_srl) {
		this.star_srl = star_srl;
	}

	public String getSupport_srl() {
		return mSupport_srl;
	}

	public void setSupport_srl(String mSupport_srl) {
		this.mSupport_srl = mSupport_srl;
	}

	public EventList(String srl, String title, String text, String type, String thum, String begin, String end,
			String entry, String enterheart, String warning, String support_srl ,String star_srl) {
		mEventSrl = srl;
		mTitle = title;
		mText = text;
		mType = type;
		mThum = thum;
		mBegin = begin;
		mEnd = end;
		mEntry = entry;
		mEnterHeart = enterheart;
		mWarning = warning;
		mSupport_srl = support_srl;
		this.star_srl = star_srl;
	}

	public String getEventSrl() {
		return mEventSrl;
	}

	public void setEventSrl(String eventSrl) {
		mEventSrl = eventSrl;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getThum() {
		return mThum;
	}

	public void setThum(String thum) {
		mThum = thum;
	}

	public String getBegin() {
		return mBegin;
	}

	public void setBegin(String begin) {
		mBegin = begin;
	}

	public String getEnd() {
		return mEnd;
	}

	public void setEnd(String end) {
		mEnd = end;
	}

	public String getEntry() {
		return mEntry;
	}

	public void setEntry(String entry) {
		mEntry = entry;
	}

	public String getEnterHeart() {
		return mEnterHeart;
	}

	public void setEnterHeart(String enterHeart) {
		mEnterHeart = enterHeart;
	}

	public String getWarning() {
		return mWarning;
	}

	public void setWarning(String warning) {
		mWarning = warning;
	}

	private void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		mText = in.readString();
		mEventSrl = in.readString();
		mThum = in.readString();
		mBegin = in.readString();
		mTitle = in.readString();
		mEnd = in.readString();
		mThum = in.readString();

		mType = in.readString();
		mEnterHeart = in.readString();
		mEntry = in.readString();
		mWarning = in.readString();
		mSupport_srl = in.readString();
		star_srl = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mText);
		dest.writeString(mEventSrl);
		dest.writeString(mThum);
		dest.writeString(mBegin);
		dest.writeString(mEnd);
		dest.writeString(mThum);
		dest.writeString(mType);
		dest.writeString(mEnterHeart);
		dest.writeString(mEntry);
		dest.writeString(mWarning);
		dest.writeString(mSupport_srl);
		dest.writeString(star_srl);
	}

	public EventList(Parcel in) {
		// TODO Auto-generated constructor stub
		readFromParcel(in);
	}

	public static final Creator<EventList> CREATOR = new Creator<EventList>() {
		public EventList createFromParcel(Parcel in) {
			return new EventList(in);
		}

		public EventList[] newArray(int size) {
			return new EventList[size];
		}
	};

}
