package shts.jp.android.nogifeed.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import shts.jp.android.nogifeed.R;

public class Showcase extends FrameLayout {
    private static final String TAG = Showcase.class.getSimpleName();

    private ViewPager mViewPager;
    private ViewPageIndicator mViewPageIndicator;
    private List<String> mImageUrls;
    private FavoriteView mFavoriteCheckbox;
    private final FavoriteChangeListener mListener;

    public interface FavoriteChangeListener {
        public void onCheckdChanged(CompoundButton compoundButton, boolean b);
    }

    public Showcase(Context context, List<String> imageUrls, FavoriteChangeListener listener) {
        this(context, null, imageUrls, listener);
    }

    public Showcase(Context context, AttributeSet attrs, List<String> imageUrls, FavoriteChangeListener listener) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.showcase, this);
        mListener = listener;
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPageIndicator = (shts.jp.android.nogifeed.views.ViewPageIndicator) findViewById(R.id.indicator);
        mFavoriteCheckbox = (shts.jp.android.nogifeed.views.FavoriteView) findViewById(R.id.favorite);
        mFavoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mListener.onCheckdChanged(compoundButton, b);
                shts.jp.android.nogifeed.utils.TrackerUtils.sendTrack(getContext(), TAG,
                        "OnClicked", "-> Favorite : " + "isFavorite(" + b + ")");
            }
        });
        mImageUrls = imageUrls;
        setupAdapter();
    }

    public void setFavorite(boolean isChecked) {
        mFavoriteCheckbox.setChecked(isChecked);
    }

    private void setupAdapter() {
        CustomPageAdapter adapter = new CustomPageAdapter(getContext(), mImageUrls);
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mViewPageIndicator.setCurrentPosition(position);
            }
        });
        mViewPageIndicator.setCount(adapter.getCount());
    }

    public class CustomPageAdapter extends PagerAdapter {

        Context mContext;
        private final List<String> mImageUrls;

        public CustomPageAdapter(Context context, List<String> imageUrls) {
            mContext = context;
            mImageUrls = imageUrls;

            for (String s : mImageUrls) {
                shts.jp.android.nogifeed.common.Logger.d(TAG, "url : " + s);
            }
        }

        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return ((View)o).equals(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Log.d(TAG, "instantiateItem called : position(" + position + ")");
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            shts.jp.android.nogifeed.utils.PicassoHelper.load(mContext, iv, mImageUrls.get(position));
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }

}
