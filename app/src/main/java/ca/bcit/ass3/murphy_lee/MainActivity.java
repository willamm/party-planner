package ca.bcit.ass3.murphy_lee;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PartyFragment.OnFragmentInteractionListener {

    public static final String EDIT_EVENT_KEY = "edit_event";
    private boolean isLargeLayout;
    private ListView eventView;
    private SQLiteDatabase db;
    String[] eventToAddItem;
    String[] eventToModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar_main);
        setSupportActionBar(tb);

        eventView = findViewById(R.id.events);
        registerForContextMenu(eventView);

        isLargeLayout = getResources().getBoolean(R.bool.large_layout);

        List<String> items = new ArrayList<>();

        updateEventList(getEventListAll());
        /*
        PartyDbHelper helper = PartyDbHelper.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(PartyContract.EventMaster.TABLE_NAME,
                new String[] {PartyContract.EventMaster._ID,PartyContract.EventMaster.NAME}, null,null,null,null,null);

        final List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.NAME));
            list.add(id + " " + name);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        eventView.setAdapter(adapter);
        */

        eventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle b = new Bundle();
                String str = (String) eventView.getItemAtPosition(i);
                eventToAddItem = str.split("\n");
                b.putInt("EVENT_ID", Integer.valueOf(eventToAddItem[0]));

                PartyFragment partyFragment = PartyFragment.newInstance(b);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(android.R.id.content, partyFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        //search stuff

        final MenuItem searchItem = menu.findItem(R.id.action_search_events);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Events");

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                MainActivity.this.setItemsVisibility(menu, searchItem, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                MainActivity.this.setItemsVisibility(menu, searchItem, true);
                return true;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                String sqlQuery = getEventListByKeyword(newText);
                return updateEventList(sqlQuery);
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                String sqlQuery = getEventListByKeyword(query);
                if (!updateEventList(sqlQuery)) {
                    Toast.makeText(MainActivity.this,"No records found!", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    Toast.makeText(MainActivity.this, "Records found!",Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem currentItem = menu.getItem(i);
            if (exception != currentItem) {
                currentItem.setVisible(visible);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_event: {
                FragmentManager fragmentManager = getSupportFragmentManager();
                NewEventFragment newEventFragment = new NewEventFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.add(android.R.id.content, newEventFragment).addToBackStack(null).commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String getEventListAll() {
        return "SELECT " +
                PartyContract.EventMaster._ID + ", " +
                PartyContract.EventMaster.NAME + ", " +
                PartyContract.EventMaster.DATE + ", " +
                PartyContract.EventMaster.TIME +
                " FROM " + PartyContract.EventMaster.TABLE_NAME;
    }

    public String getEventListByKeyword(String query) {
        return "SELECT " +
                PartyContract.EventMaster._ID + ", " +
                PartyContract.EventMaster.NAME + ", " +
                PartyContract.EventMaster.DATE + ", " +
                PartyContract.EventMaster.TIME +
                " FROM " + PartyContract.EventMaster.TABLE_NAME +
                " WHERE " + PartyContract.EventMaster.NAME + " LIKE '%" + query + "%' " +
                " OR " + PartyContract.EventMaster.DATE + " LIKE '%" + query + "%' " +
                " OR " + PartyContract.EventMaster.TIME + " LIKE '%" + query + "%' ";
    }

    public boolean updateEventList(String sqlQuery) {
        PartyDbHelper helper = PartyDbHelper.getInstance(this);;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        final List<String> list = new ArrayList<>();
        boolean somethingToShow = false;

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.TIME));
                list.add(id + "\n" + name + "\n" + date + "\n" + time);
            } while (cursor.moveToNext());
            somethingToShow = true;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        eventView.setAdapter(adapter);
        cursor.close();
        db.close();
        return somethingToShow;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == eventView.getId()) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String str = (String) eventView.getItemAtPosition(acmi.position);
            eventToModify = str.split("\n");
            menu.add("Edit");
            menu.add("Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Edit") {
            FragmentManager fragmentManager = getSupportFragmentManager();
            NewEventFragment newEventFragment = new NewEventFragment();
            Bundle args = new Bundle();
            args.putStringArray(EDIT_EVENT_KEY, eventToModify);
            newEventFragment.setArguments(args);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.add(android.R.id.content, newEventFragment).addToBackStack(null).commit();
            return true;
        } else if (item.getTitle() == "Delete") {
            int success = db.delete(
                    PartyContract.EventMaster.TABLE_NAME,
                    PartyContract.EventMaster._ID + "=" + eventToModify[0],
                    null);
            if (success > 0) {
                updateEventList(getEventListAll());
            }
            return success > 0;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}
