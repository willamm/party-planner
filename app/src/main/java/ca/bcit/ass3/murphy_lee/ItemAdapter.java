package ca.bcit.ass3.murphy_lee;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

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
    public void remove(String str) {
        super.remove(str);
        String[] toArray = str.split("\n");
        String name = toArray[1];
        String whereClause = PartyContract.EventDetails.ITEM_NAME + "=?";
        String[] whereArgs = new String[] {name};
        SQLiteDatabase db = PartyDbHelper.getInstance(getContext()).getWritableDatabase();
        db.delete(PartyContract.EventDetails.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void updateListView(ContentValues values) {
        if (values != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(values.get(PartyContract.EventDetails.ITEM_NAME));
            stringBuilder.append("\n");
            stringBuilder.append(values.get(PartyContract.EventDetails.ITEM_UNIT));
            stringBuilder.append("\n");
            stringBuilder.append(values.get(PartyContract.EventDetails.ITEM_QUANTITY));
            mItemList.add(stringBuilder.toString());
        }
        super.notifyDataSetChanged();
    }

}
