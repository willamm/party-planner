package ca.bcit.ass3.murphy_lastname2;

import android.provider.BaseColumns;

final class PartyContract {

    private PartyContract() {
    }

    static class Event implements BaseColumns {
        static final String TABLE_NAME = "event";
        static final String NAME = "name";
        static final String DATE = "date";
        static final String TIME = "time";
    }

    static class EventDetails implements BaseColumns {
        static final String TABLE_NAME = "detail";
        static final String ITEM_NAME = "item_name";
        static final String ITEM_UNIT = "item_unit";
        static final String ITEM_QUANTITY = "item_quantity";
        static final String EVENT_ID = "event_id";
    }

    static class Contribution implements BaseColumns {
        static final String TABLE_NAME = "contribution";
        static final String NAME = "name";
        static final String QUANTITY = "quantity";
        static final String DATE = "date";
        static final String DETAIL_ID = "detail_id";
    }


}
