package talespin.test.hackathonday1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddYourBitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddYourBitFragment extends Fragment {

    private Story story;

    public AddYourBitFragment() {
    }

    public static AddYourBitFragment newInstance() {
        AddYourBitFragment fragment = new AddYourBitFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        story = ((StoryWriteActivity) getActivity()).getStory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_your_bit, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText draftTextView = (EditText) getActivity().findViewById(R.id.add_your_bit);
        draftTextView.setText(story.getDraft());
    }

    @Override
    public void onPause() {
        super.onPause();
        EditText draftTextView = (EditText) getActivity().findViewById(R.id.add_your_bit);
        story.setDraft(draftTextView.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
