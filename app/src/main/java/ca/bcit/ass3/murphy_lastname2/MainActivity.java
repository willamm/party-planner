package ca.bcit.ass3.murphy_lastname2;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CreateEventFragment.OnFragmentInteractionListener {

    private boolean isLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ListView lv  = findViewById(R.id.events);

        isLargeLayout = getResources().getBoolean(R.bool.large_layout);
        //Reading
        SQLiteDatabase dbRead = new PartyDbHelper(this).getReadableDatabase();

        String[] projection = {
                PartyContract.EventMaster.NAME,
                PartyContract.EventMaster.DATE,
                PartyContract.EventMaster.TIME
        };

        Cursor cursor = dbRead.query(true,
                PartyContract.EventMaster.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        List<String> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            String temp = cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.NAME));
            temp += " " + cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.DATE));
            temp += " " + cursor.getString(cursor.getColumnIndexOrThrow(PartyContract.EventMaster.TIME));
            items.add(temp);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
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
                showDialog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        AppCompatDialogFragment newFragment = new CreateEventFragment();

        if (isLargeLayout) {
            newFragment.show(fragmentManager, "dialog");
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
