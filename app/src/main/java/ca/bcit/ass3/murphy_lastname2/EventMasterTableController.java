package ca.bcit.ass3.murphy_lastname2;

import android.content.Context;
import android.database.Cursor;

public class EventMasterTableController extends PartyDbHelper {

    public EventMasterTableController(Context context) {
        super(context);
    }

    public boolean createEvent(EventMaster em) {
        return false;
    }

    public boolean updateEvent() {
        return false;
    }

    public Cursor readEvent() {
        Cursor result = null;
        return result;
    }
}
