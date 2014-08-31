package talespin.test.hackathonday1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class StoryAdapter extends ArrayAdapter<Story> implements ListAdapter {
    private final Context context;
    private final List<Story> values;

    public StoryAdapter(Context context, List<Story> values) {
        super(context, R.layout.my_turn, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_turn, parent, false);
        TextView titleTextView = (TextView) rowView.findViewById(R.id.story_title);
        TextView infoTextView = (TextView) rowView.findViewById(R.id.story_info);
        String title = values.get(position).getTitle();
        if (title.length() == 0) title = "Add your title!";
        String info = values.get(position).getInfo();
        if (info.length() == 0) info = "Add details!";
        titleTextView.setText(title);
        infoTextView.setText(info);
        return rowView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}