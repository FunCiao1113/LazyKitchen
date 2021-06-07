package com.example.lazykitchen.fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SearchView;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.Adapter;
import com.example.lazykitchen.util.VideoItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private Fragment[] FragmentArrays = new Fragment[3];
    private String[] TabTitles = new String[3];

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.search_fragment,null);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.pager);
        initialView(tabLayout,viewPager);
        SearchView searchView = view.findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("输入您想查找的内容");
        return view;
    }

    private void initialView(TabLayout tabLayout, ViewPager viewPager){
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        TabTitles[0] = "食谱大全";
        TabTitles[1] = "做饭技巧";
        TabTitles[2] = "选购指南";
        FragmentArrays[0] = CookFragment.newInstance();
        FragmentArrays[1] = SkillFragment.newInstance();
        FragmentArrays[2] = BuyFragment.newInstance();
        PagerAdapter pagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //将ViewPager和TabLayout绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    final class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentArrays[position];
        }


        @Override
        public int getCount() {
            return FragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TabTitles[position];

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        // TODO: Use the ViewModel
    }

}