package ca.bcit.ass3.murphy_lastname2;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 2017-11-10.
 */

public class ItemAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mItemList;
    private int mLayoutResourceId;

    public ItemAdapter(Context context, int layoutResourceId, List<String> list) {
        super(context, layoutResourceId, list);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mItemList = list;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((AppCompatActivity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.item = row.findViewById(R.id.list_item_name);
            holder.unit = row.findViewById(R.id.list_item_unit);
            holder.quantity = row.findViewById(R.id.list_item_quantity);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }
        String[] itemDetails = mItemList.get(position).split(" ");
        if (itemDetails.length == 0) {
            return row;
        }
        holder.item.setText(itemDetails[0]);
        holder.unit.setText(itemDetails[1]);
        holder.quantity.setText(itemDetails[2]);
        return row;
    }

    public void deleteItem(int position) {
        mItemList.remove(position);
        super.notifyDataSetChanged();
    }

    public void editItem(int position) {

    }

    public void updateListView(ContentValues values) {
        if (values != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(values.get(PartyContract.EventDetails.ITEM_NAME));
            stringBuilder.append(" ");
            stringBuilder.append(values.get(PartyContract.EventDetails.ITEM_UNIT));
            stringBuilder.append(" ");
            stringBuilder.append(values.get(PartyContract.EventDetails.ITEM_QUANTITY));
            mItemList.add(stringBuilder.toString());
        }
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView item;
        private TextView unit;
        private TextView quantity;

        public ViewHolder() {
        }
    }
}
