package org.xiangbalao.common.parallexheaderviewpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.xiangbalao.common.adapter.BaseRecyclerAdapter;
import org.xiangbalao.common.adapter.SmartViewHolder;

import java.util.Arrays;
import java.util.Collection;

import static android.R.layout.simple_list_item_2;

public class SecendActivity extends AppCompatActivity {

    private SmartPagerAdapter mAdapter;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private SmartRefreshLayout refreshLayout;
    private PagerSlidingTabStrip tabs;

    public static void start(Context context) {
        Intent starter = new Intent(context, SecendActivity.class);

        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_example_nestedscroll_integral);
        initView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);


        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });


        mViewPager.setAdapter(mAdapter = new SmartPagerAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        tabs.setViewPager(mViewPager);

    }

    public static class SmartPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES = {"Page 1", "Page 2", "Page 3", "Page 4"};

        private final SmartFragment[] fragments;

        SmartPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new SmartFragment[]{
                    new SmartFragment(), new SmartFragment()
            };
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }


        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }
    }


    public static class SmartFragment extends Fragment {

        private RecyclerView mRecyclerView;
        private BaseRecyclerAdapter<Void> mAdapter;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return new RecyclerView(inflater.getContext());
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mRecyclerView = (RecyclerView) view;

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Void>(initData(), simple_list_item_2) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Void model, int position) {
                    holder.text(android.R.id.text1, getString(R.string.item_example_number_title, position));
                    holder.text(android.R.id.text2, getString(R.string.item_example_number_abstract, position));
                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
                }
            });
        }

        private Collection<Void> initData() {
            return Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        public void onRefresh(final RefreshLayout refreshLayout) {
            refreshLayout.getLayout().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.refresh(initData());
                    refreshLayout.finishRefresh();
                    refreshLayout.resetNoMoreData();//setNoMoreData(false);
                }
            }, 2000);
        }

        public void onLoadMore(final RefreshLayout refreshLayout) {
            refreshLayout.getLayout().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.loadMore(initData());
                    if (mAdapter.getItemCount() > 60) {
                        Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                        refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            }, 2000);
        }
    }

}
