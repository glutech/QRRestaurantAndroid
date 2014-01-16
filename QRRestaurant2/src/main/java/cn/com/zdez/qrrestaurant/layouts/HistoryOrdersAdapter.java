package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.vo.HistoryVo;

/**
 * Created by LuoHanLin on 13-12-27.
 */
public class HistoryOrdersAdapter extends ArrayAdapter<HistoryVo> {

    private static String TAG = HistoryOrdersAdapter.class.getSimpleName();
    private Context context;

    public HistoryOrdersAdapter(Context context, int resource, HistoryVo[] objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoyOrdersListItem v;

        v = (HistoyOrdersListItem) View.inflate(context, R.layout.list_item_history_order, null);
        v.setLayout(getItem(position));

        return v;
    }
}
