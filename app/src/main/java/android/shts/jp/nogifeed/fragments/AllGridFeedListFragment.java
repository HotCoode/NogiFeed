package android.shts.jp.nogifeed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.shts.jp.nogifeed.R;
import android.shts.jp.nogifeed.adapters.BindableAdapter;
import android.shts.jp.nogifeed.adapters.RecyclableAdapter;
import android.shts.jp.nogifeed.api.AsyncRssClient;
import android.shts.jp.nogifeed.common.Logger;
import android.shts.jp.nogifeed.listener.RssClientListener;
import android.shts.jp.nogifeed.models.Entries;
import android.shts.jp.nogifeed.models.Member;
import android.shts.jp.nogifeed.utils.IntentUtils;
import android.shts.jp.nogifeed.utils.JsoupUtils;
import android.shts.jp.nogifeed.utils.PicassoHelper;
import android.shts.jp.nogifeed.utils.UrlUtils;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;

import java.util.List;

/**
 * For tablet layout.
 */
public class AllGridFeedListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = AllMemberGridListFragment.class.getSimpleName();
    private GridView mMemberList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_all_member_list, null);
        mMemberList = (GridView) view.findViewById(R.id.gridview);
        mMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Member member = (Member) mMemberList.getItemAtPosition(i);
            }
        });
        // SwipeRefreshLayoutの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.nogifeed, R.color.nogifeed, R.color.nogifeed, R.color.nogifeed);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAllFeed();
    }

    private void getAllFeed() {
        boolean ret = AsyncRssClient.read(getActivity().getApplicationContext(),
                UrlUtils.FEED_ALL_URL, new RssClientListener() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Entries entries) {
                        Logger.i("getAllFeed()", "get all member feed : size(" + entries.size() + ")");
                        // refresh feed list
                        setupAdapter(entries);
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Show error toast
                        Toast.makeText(getActivity(), getResources().getString(R.string.feed_failure),
                                Toast.LENGTH_SHORT).show();
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

        if (!ret) {
            // Show error toast
            Toast.makeText(getActivity(), getResources().getString(R.string.feed_failure),
                    Toast.LENGTH_SHORT).show();
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private void setupAdapter(Entries entries) {
        //mMemberList.setAdapter(new GridAdapter(getActivity(), entries));
    }

    @Override
    public void onRefresh() {
        getAllFeed();
    }


    class GridAdapter extends RecyclableAdapter {

        private final Context mContext;
        private final Entries mEntries;

        GridAdapter(Context context, Entries entries) {
            super(context, entries);
            mContext = context;
            mEntries = entries;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Object object) {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            return null;
        }

        class ViewHolder {
            public TextView titleTextView;
            public ImageView profileImageView;
            public ImageView favoriteImageView;
            ViewHolder (View view) {
                titleTextView = (TextView) view.findViewById(R.id.member_name);
                profileImageView = (ImageView) view.findViewById(R.id.profile_image);
                favoriteImageView = (ImageView) view.findViewById(R.id.favorite_icon);
            }
        }

        //@Override
        public View newView(LayoutInflater inflater, int position, ViewGroup container) {
            View view = inflater.inflate(R.layout.list_item_member, null);
            final ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
            return view;
        }

        //@Override
        public void bindView(Object item, int position, View view) {
            final ViewHolder holder = (ViewHolder) view.getTag();
            final Member member = (Member) item;
            Logger.i(TAG, member.toString());

            holder.titleTextView.setText(member.name);
            holder.favoriteImageView.setVisibility(
                    member.isFavorite(mContext) ? View.VISIBLE : View.GONE/*View.VISIBLE*/);

            if (TextUtils.isEmpty(member.profileImageUrl)) {
                holder.profileImageView.setImageResource(R.drawable.kensyusei);
            } else {
                PicassoHelper.loadAndCircleTransform(
                        mContext, holder.profileImageView, member.profileImageUrl);
            }
        }
    }
}