package ca.bcit.ass3.murphy_lastname2;

import android.provider.BaseColumns;

/**
 * Created by willm_000 on 2017-10-29.
 */

public final class PartyContract {

    private PartyContract() {
    }

    public static class Event implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String NAME = "name";
        public static final String DATE = "date";
        public static final String TIME = "time";
    }

    public static class EventDetails implements BaseColumns {
        public static final String TABLE_NAME = "detail";
        public static final String ITEM_NAME = "item_name";
        public static final String ITEM_UNIT = "item_unit";
        public static final String ITEM_QUANTITY = "item_quantity";
        public static final String EVENT_ID = "event_id";
    }

    public static class Contribution implements BaseColumns {
        public static final String TABLE_NAME = "contribution";
        public static final String NAME = "name";
        public static final String QUANTITY = "quantity";
        public static final String DATE = "date";
        public static final String DETAIL_ID = "detail_id";
    }


}
