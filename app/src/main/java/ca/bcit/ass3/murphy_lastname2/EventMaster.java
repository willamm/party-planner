package ca.bcit.ass3.murphy_lastname2;

import java.sql.Date;
import java.sql.Time;

public class EventMaster {

    private String mName;
    private Date mDate;
    private Time mTime;

    public EventMaster(String name, Date date, Time time) {
        mName = name;
        mDate = date;
        mTime = time;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public Time getTime() {
        return mTime;
    }

    public void setTime(Time mTime) {
        this.mTime = mTime;
    }
}
