package ca.bcit.ass3.murphy_lastname2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link PartyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyFragment extends ListFragment {

    private PartyDbHelper partyDbHelper;
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

    private static final String JOIN_ON_EVENT_ID_QUERY =
            "SELECT " + PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails.ITEM_NAME +
            " FROM " + PartyContract.EventDetails.TABLE_NAME +
            " JOIN " + PartyContract.EventMaster.TABLE_NAME +
            " ON " + PartyContract.EventDetails.TABLE_NAME + "." + PartyContract.EventDetails.EVENT_ID
            + "=" + PartyContract.EventMaster.TABLE_NAME + "." + PartyContract.EventMaster._ID + ";";

    public PartyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyFragment newInstance(String param1, String param2) {
        PartyFragment fragment = new PartyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partyDbHelper = new PartyDbHelper(getActivity());
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

        //Display party items in ListView
        SQLiteDatabase db = partyDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(JOIN_ON_EVENT_ID_QUERY
                , null);
        List<String> itemList = new ArrayList<>();
        while(cursor.moveToNext()) {
            itemList.add(cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventDetails.ITEM_NAME)));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, itemList);

        setListAdapter(adapter);

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
                values.put(PartyContract.EventDetails.ITEM_NAME, itemName.getText().toString());
                values.put(PartyContract.EventDetails.ITEM_UNIT, itemType.getText().toString());
                values.put(PartyContract.EventDetails.ITEM_QUANTITY, itemQuantity.getText().toString());
                //put id here
                values.put(PartyContract.EventDetails.EVENT_ID, PartyFragment.this.getArguments().getInt("EVENT_ID"));

                SQLiteDatabase rdb = partyDbHelper.getWritableDatabase();
                rdb.insertWithOnConflict(PartyContract.EventDetails.TABLE_NAME
                        , null
                        , values
                        , SQLiteDatabase.CONFLICT_REPLACE);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
