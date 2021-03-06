package shts.jp.android.nogifeed.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import shts.jp.android.nogifeed.R;
import shts.jp.android.nogifeed.services.ImageDownloader;
import shts.jp.android.nogifeed.views.notifications.BlogUpdateNotification;

public class BlogActivity extends shts.jp.android.nogifeed.activities.BaseActivity {

    private shts.jp.android.nogifeed.models.Entry mEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        Bundle bundle = new Bundle();
        shts.jp.android.nogifeed.models.Entry entry = getIntent().getParcelableExtra(shts.jp.android.nogifeed.models.Entry.KEY);
        if (entry != null) {
            bundle.putParcelable(shts.jp.android.nogifeed.models.Entry.KEY, entry);
        } else {
            String blogUrl = getIntent().getStringExtra(BlogUpdateNotification.KEY);
            bundle.putString(BlogUpdateNotification.KEY, blogUrl);
        }

        shts.jp.android.nogifeed.fragments.BlogFragment blogFragment = new shts.jp.android.nogifeed.fragments.BlogFragment();
        blogFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, blogFragment, shts.jp.android.nogifeed.fragments.BlogFragment.class.getSimpleName());
        ft.commit();

        setupActionBar();

        mEntry = entry;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.activity_blog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ImageDownloader.downloads(getApplicationContext(), mEntry);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Activity getTrackerActivity() {
        return BlogActivity.this;
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            shts.jp.android.nogifeed.fragments.BlogFragment blogFragment =
                    (shts.jp.android.nogifeed.fragments.BlogFragment) getSupportFragmentManager().findFragmentByTag(
                            shts.jp.android.nogifeed.fragments.BlogFragment.class.getSimpleName());

            if (blogFragment != null) {
                if (blogFragment.isVisible()) {
                    if (blogFragment.goBack()) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
