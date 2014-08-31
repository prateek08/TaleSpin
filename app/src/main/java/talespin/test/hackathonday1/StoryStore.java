package talespin.test.hackathonday1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pragar on 9/1/2014.
 */
public class StoryStore {

    private List<Story> myStoryList;
    private List<Story> theirStoryList;

    public StoryStore() {
        myStoryList = new ArrayList<Story>();
        theirStoryList = new ArrayList<Story>();
    }

    public int addMyStory(Story story) {
        myStoryList.add(story);
        return myStoryList.size() - 1;
    }

    public int addTheirStory(Story story) {
        theirStoryList.add(story);
        return theirStoryList.size() - 1;
    }

    public List<Story> getMyStories() {
        return myStoryList;
    }

    public List<Story> getTheirStories() {
        return theirStoryList;
    }

    public void reset() {
        myStoryList.clear();
        theirStoryList.clear();
    }
}
