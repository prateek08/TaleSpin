package com.example.test.hackathonday1;

/**
 * Created by pragar on 7/30/2014.
 */
public class SingleListItem {
    String Title;
    private long Time;
    public SingleListItem(String title, long time)
    {
        Title = title;
        setTime(time);
    }

    public String getTime() {
        long diffInSeconds = Time;

        long diff[] = new long[] { 0, 0, 0, 0 };
    /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
    /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
    /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
    /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));

        long diffToSelect = 0;
        String id = "Just a while";
        if(diff[0] > 0)
        {
            diffToSelect = diff[0];
            id = "Days";
        }
        else if(diff[1] > 0)
        {
            diffToSelect = diff[1];
            id = "Hours";
        }
        else if(diff[2] > 0)
        {
            diffToSelect = diff[2];
            id = "Minutes";
        }
        else if(diff[3] > 0)
        {
            diffToSelect = diff[3];
            id = "Seconds";
        }

        return String.format("%d %s ago", diffToSelect, id);
        /*return String.format(
                "%d day%s, %d hour%s, %d minute%s, %d second%s ago",
                diff[0],
                diff[0] > 1 ? "s" : "",
                diff[1],
                diff[1] > 1 ? "s" : "",
                diff[2],
                diff[2] > 1 ? "s" : "",
                diff[3],
                diff[3] > 1 ? "s" : "");*/


    }

    public void setTime(long time) {
        Time = Math.abs(time);//TODO: check time difference
    }
}
