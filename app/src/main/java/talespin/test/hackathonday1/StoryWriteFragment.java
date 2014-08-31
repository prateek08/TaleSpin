package talespin.test.hackathonday1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pragar on 8/31/2014.
 */
public class StoryWriteFragment extends Fragment {

    private Story story;

    public StoryWriteFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StoryWriteFragment newInstance() {
        StoryWriteFragment fragment = new StoryWriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StoryWriteActivity parent = (StoryWriteActivity) getActivity();
        this.story = parent.getStory();
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView titleView = (TextView) getActivity().findViewById(R.id.story_title);
        TextView infoView = (TextView) getActivity().findViewById(R.id.story_data);
        titleView.setText(story.getTitle());
        if (story.getDraft().length() > 0) {
            infoView.setText(story.getInfo() + " [ " + story.getDraft() + " ]");
        } else {
            infoView.setText(story.getInfo());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_write, container, false);
        return rootView;
    }
}
