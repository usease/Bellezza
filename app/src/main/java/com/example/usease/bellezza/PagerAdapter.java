package com.example.usease.bellezza;

import android.content.Context;
import android.os.LocaleList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

public class PagerAdapter extends FragmentPagerAdapter {


    private Context context;

    public PagerAdapter(FragmentManager fm, Context contex) {
        super(fm);
        this.context = contex;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllProductsFragment allProductsFragment = new AllProductsFragment();
                return allProductsFragment;

            case 1:
                NewProductsFragment newProductsFragment =  new NewProductsFragment();
                return  newProductsFragment;

            default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    //Method responsible for setting titles for the tabs
    public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return  context.getResources().getString(R.string.all_products);
                case 1:
                    return context.getResources().getString(R.string.new_products);
                default:
                    return null;
            }

    }
}
