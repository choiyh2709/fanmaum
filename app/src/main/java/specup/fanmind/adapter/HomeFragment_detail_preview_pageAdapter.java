package specup.fanmind.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.MovieList;


public class HomeFragment_detail_preview_pageAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;

    ImageView imageView;
    List<Object> list;
    View.OnClickListener onclicklistener;

    public HomeFragment_detail_preview_pageAdapter(Context context, List<Object> list,  View.OnClickListener onclicklistener) {
        mContext = context;
        this.onclicklistener = onclicklistener;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.viewpager_preview, container, false);

        imageView = (ImageView) view.findViewById(R.id.ImageView_Background);
        imageView.setOnClickListener(onclicklistener);
        try {
            HttpRequest.glideImage(mContext,((MovieList) list.get(position)).getImagePath(), imageView);
            imageView.setTag(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
