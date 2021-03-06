package ca.bcit.ass3.murphy_lee;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PartyFragment extends ListFragment {

    private ArrayAdapter<String> itemAdapter;
    private PartyDbHelper partyDbHelper;
    private SQLiteDatabase db;

    // EditText Views to add item
    private EditText itemName;
    private EditText itemType;
    private EditText itemQuantity;

    private ListView listView;

    public PartyFragment() {
        // Required empty public constructor
    }

    public static PartyFragment newInstance(Bundle bundle) {
        PartyFragment fragment = new PartyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partyDbHelper = PartyDbHelper.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_party, container, false);

        Toolbar partyToolbar = rootView.findViewById(R.id.toolbar_party);
        partyToolbar.setTitle(R.string.party_fragment_title);

        FloatingActionButton addItemsButton = rootView.findViewById(R.id.add_party_items);
        String[] columns = new String[]{PartyContract.EventDetails._ID
                , PartyContract.EventDetails.ITEM_NAME
                , PartyContract.EventDetails.ITEM_UNIT
                , PartyContract.EventDetails.ITEM_QUANTITY};
        db = partyDbHelper.getReadableDatabase();
        Cursor cursor = db.query(PartyContract.EventDetails.TABLE_NAME
                , columns
                , PartyContract.EventDetails.EVENT_ID + "=?"
                , new String[]{this.getArguments().getInt("EVENT_ID") + ""}
                , null
                , null
                , null);
        List<String> itemList = new ArrayList<>();
        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails.ITEM_NAME));
            String itemUnit = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails.ITEM_UNIT));
            String itemQuantity = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails.ITEM_QUANTITY));
            itemList.add(id + "\n" + itemName + "\n" + itemUnit + "\n" + itemQuantity);
        }
        itemAdapter = new ItemAdapter(getContext(), android.R.layout.simple_list_item_1, itemList);
        setListAdapter(itemAdapter);

        cursor.close();

        addItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddItemDialog();
            }
        });
        return rootView;
    }

    private void openAddItemDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        View addItemView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_item, null);
        alertDialog.setView(addItemView);
        alertDialog.setTitle(R.string.add_item_title);

        itemName = addItemView.findViewById(R.id.itemName);
        itemType = addItemView.findViewById(R.id.itemType);
        itemQuantity = addItemView.findViewById(R.id.itemQuantity);

        alertDialog.setPositiveButton(R.string.add_item_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ContentValues values = new ContentValues();
                int event_id = PartyFragment.this.getArguments().getInt("EVENT_ID");
                if (!TextUtils.isEmpty(itemName.getText()) && !TextUtils.isEmpty(itemType.getText())
                        && !TextUtils.isEmpty(itemQuantity.getText())) {
                    values.put(PartyContract.EventDetails.ITEM_NAME, itemName.getText().toString());
                    values.put(PartyContract.EventDetails.ITEM_UNIT, itemType.getText().toString());
                    values.put(PartyContract.EventDetails.ITEM_QUANTITY, itemQuantity.getText().toString());
                    values.put(PartyContract.EventDetails.EVENT_ID, event_id);
                    db = partyDbHelper.getWritableDatabase();
                    db.insertWithOnConflict(PartyContract.EventDetails.TABLE_NAME
                            , null
                            , values
                            , SQLiteDatabase.CONFLICT_REPLACE);

                    ((ItemAdapter) itemAdapter).updateListView(values);
                } else {
                    Toast.makeText(getContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton(R.string.add_item_cancel, null);
        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getListView();
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == listView.getId()) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.item_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selected = itemAdapter.getItem(acmi.position);
        switch(item.getItemId()) {
            case R.id.delete_item: {
                itemAdapter.remove(selected);
                return true;
            }
            case R.id.edit_item: {
                itemAdapter.remove(selected);
                openAddItemDialog();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

}
