package talespin.test.hackathonday1;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;

public class MatchInitiatedCallback implements ResultCallback {

    Activity parentActivity;

    public MatchInitiatedCallback(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public void onResult(Result _result) {
        TurnBasedMultiplayer.InitiateMatchResult result = (TurnBasedMultiplayer.InitiateMatchResult) _result;
        // Check if the status code is not success;
        if (!result.getStatus().isSuccess()) {
            //showError(statusCode);
            return;
        }

        TurnBasedMatch match = result.getMatch();

//        // If this player is not the first player in this match, continue.
//        if (match.getData() != null) {
//            // showTurnUI(match);
//            return;
//        }

        Story story = new Story("", "");
        int position = ListStories.storyStore.addMyStory(story);
        // Otherwise, this is the first player. Initialize the game state.
        Intent intent = new Intent(parentActivity, StoryWriteActivity.class);
        intent.putExtra("storyPosition", position);
        parentActivity.startActivity(intent);
    }
}
