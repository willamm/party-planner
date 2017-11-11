package ca.bcit.ass3.murphy_lee;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Activities that contain this fragment must implement the
 * {@link PartyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyFragment extends ListFragment {

    private ItemAdapter itemAdapter;
    private PartyDbHelper partyDbHelper;
    private SQLiteDatabase db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // EditText Views to add item
    private EditText itemName;
    private EditText itemType;
    private EditText itemQuantity;

    private ListView listView;

    private static final String JOIN_ON_EVENT_ID_QUERY =
            "SELECT " + PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails._ID + "," +
                        PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails.ITEM_NAME + "," +
                        PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails.ITEM_UNIT  + "," +
                        PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails.ITEM_QUANTITY + "," +
                        PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails.EVENT_ID +
            " FROM " + PartyContract.EventDetails.TABLE_NAME +
            " JOIN " + PartyContract.EventMaster.TABLE_NAME +
            " ON " + PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails.EVENT_ID
            + "=" + PartyContract.EventMaster.TABLE_NAME + "." + PartyContract.EventMaster._ID + ";";

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_party, container, false);

        Toolbar partyToolbar = rootView.findViewById(R.id.toolbar_party);
        partyToolbar.setTitle(R.string.party_fragment_title);

        FloatingActionButton addItemsButton = rootView.findViewById(R.id.add_party_items);

        db = partyDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(JOIN_ON_EVENT_ID_QUERY
                , null);
        List<String> itemList = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails.ITEM_NAME));
            String itemUnit = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails.ITEM_UNIT));
            String itemQuantity = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails.ITEM_QUANTITY));
            itemList.add(itemName + " " + itemUnit + " " + itemQuantity);
        }
        itemAdapter = new ItemAdapter(getContext(), R.layout.layout_items, itemList);
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
        alertDialog.setTitle("Add new item");

        itemName = addItemView.findViewById(R.id.itemName);
        itemType = addItemView.findViewById(R.id.itemType);
        itemQuantity = addItemView.findViewById(R.id.itemQuantity);

        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ContentValues values = new ContentValues();
                if (!TextUtils.isEmpty(itemName.getText()) && !TextUtils.isEmpty(itemType.getText())
                        && !TextUtils.isEmpty(itemQuantity.getText())) {
                    values.put(PartyContract.EventDetails.ITEM_NAME, itemName.getText().toString());
                    values.put(PartyContract.EventDetails.ITEM_UNIT, itemType.getText().toString());
                    values.put(PartyContract.EventDetails.ITEM_QUANTITY, itemQuantity.getText().toString());
                    values.put(PartyContract.EventDetails.EVENT_ID, PartyFragment.this.getArguments().getInt("EVENT_ID"));

                    db = partyDbHelper.getWritableDatabase();
                    db.insertWithOnConflict(PartyContract.EventDetails.TABLE_NAME
                            , null
                            , values
                            , SQLiteDatabase.CONFLICT_REPLACE);
                    itemAdapter.updateListView(values);
                } else {
                    Toast.makeText(getContext(), "Cannot have empty fields", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", null);
        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getListView();
        registerForContextMenu(listView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        switch(item.getItemId()) {
            case R.id.delete_item: {
                String toDelete = itemAdapter.getItem(acmi.position);
                itemAdapter.remove(toDelete);
                String[] toArray = toDelete.split(" ");
                String name = toArray[0];
                String whereClause = "ITEM_NAME=?";
                String[] whereArgs = new String[] {name};
                db.delete(PartyContract.EventDetails.TABLE_NAME, whereClause, whereArgs);
                return true;
            }
            case R.id.edit_item: {

            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
