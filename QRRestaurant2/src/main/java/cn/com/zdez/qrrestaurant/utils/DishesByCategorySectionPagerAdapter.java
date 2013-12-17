package cn.com.zdez.qrrestaurant.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.RestaurantDishesListActivity;
import cn.com.zdez.qrrestaurant.model.Category;

/**
 * Created by LuoHanLin on 13-12-16.
 */

public class DishesByCategorySectionPagerAdapter extends FragmentPagerAdapter {

    public List<Category> cats;
    public Context context;

    public DishesByCategorySectionPagerAdapter(Context context, FragmentManager fm, List<Category> cats) {
        super(fm);

        this.context = context;
        this.cats = cats;
    }

    @Override
    public Fragment getItem(int i) {
        return RestaurantDishesListActivity.PlaceholderFragment.newInstance(i + 1);
    }

    @Override
    public int getCount() {
        return cats.size() + 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.pages_default_title1);
            case 1:
                return context.getString(R.string.pages_default_title2);
            default:
                return cats.get(position - 2).getCat_name();
        }
    }
}
