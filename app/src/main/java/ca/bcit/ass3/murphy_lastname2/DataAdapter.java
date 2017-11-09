package ca.bcit.ass3.murphy_lastname2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends CursorAdapter {

    public DataAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ListView listView = view.findViewById(R.id.events);
        TextView eventName = new TextView(context);
        eventName.setText(cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.NAME)));
        listView.addFooterView(eventName);
    }
}