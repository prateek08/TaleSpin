package talespin.test.hackathonday1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchBuffer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.util.ArrayList;


public class ListStories extends BaseGameActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final StoryStore storyStore = new StoryStore();
    private static final int RC_SELECT_PLAYERS = 101;
    //GoogleApiClient mGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stories);

        Games.TurnBasedMultiplayer.loadMatchesByStatus(getApiClient(),
                new int[]{TurnBasedMatch.MATCH_STATUS_ACTIVE,
                        TurnBasedMatch.MATCH_STATUS_COMPLETE,
                        TurnBasedMatch.MATCH_TURN_STATUS_INVITED}
        ).setResultCallback(new ResultCallback<TurnBasedMultiplayer.LoadMatchesResult>() {
            @Override
            public void onResult(TurnBasedMultiplayer.LoadMatchesResult loadMatchesResult) {
                showActiveGames(loadMatchesResult);
            }
        });
    }

    private void showActiveGames(TurnBasedMultiplayer.LoadMatchesResult loadMatchesResult) {
        LoadMatchesResponse matches = loadMatchesResult.getMatches();
        DisplayXMatchesData(matches);
    }

    private void DisplayXMatchesData(LoadMatchesResponse matches) {
        // Setup new story button action
        final View newGameButton = findViewById(R.id.start_new_story);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        Games.TurnBasedMultiplayer.getSelectOpponentsIntent(getApiClient(), 1, 7, true);
                startActivityForResult(intent, RC_SELECT_PLAYERS);
            }
        });
        storyStore.reset();
        final TurnBasedMatchBuffer myTurnMatches = matches.getMyTurnMatches();
        final TurnBasedMatchBuffer theirTurnMatches = matches.getTheirTurnMatches();
        if (myTurnMatches != null) {
            for (int i = 0; i < myTurnMatches.getCount(); i++) {
                Story story = new Story(SkeletonTurn.unpersist(myTurnMatches.get(i).getData()).storyTitle,
                        "TODO(myTurn): Add story info.");
                storyStore.addMyStory(story);
            }
        }

        if (theirTurnMatches != null) {
            for (int i = 0; i < theirTurnMatches.getCount(); i++) {
                Story story = new Story(SkeletonTurn.unpersist(theirTurnMatches.get(i).getData()).storyTitle,
                        "TODO(theirTurn): Add story info.");
                storyStore.addTheirStory(story);
            }
        }

        for (int i = 0; i < 2; i++) {
            storyStore.addTheirStory(new Story("Story Title: " + i, "Trial Story!"));
        }
        StoryAdapter myTurnAdapter;
        myTurnAdapter = new StoryAdapter(this, storyStore.getMyStories());
        final ListView myTurnListView = (ListView) findViewById(R.id.myTurnListView);
        myTurnListView.setAdapter(myTurnAdapter);

        myTurnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), StoryWriteActivity.class);
                intent.putExtra("storyPosition", position);
                startActivity(intent);
            }
        });
        StoryAdapter theirTurnAdapter;
        theirTurnAdapter = new StoryAdapter(this, storyStore.getTheirStories());
        final ListView theirTurnListView = (ListView) findViewById(R.id.theirTurnListView);
        theirTurnListView.setAdapter(theirTurnAdapter);

        theirTurnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);

        if (request == RC_SELECT_PLAYERS) {
            if (response != Activity.RESULT_OK) {
                // user canceled
                return;
            }

            // get the invitee list
            final ArrayList<String> invitees =
                    data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                    .addInvitedPlayers(invitees).build();

            // kick the match off
            Games.TurnBasedMultiplayer
                    .createMatch(getApiClient(), tbmc)
                    .setResultCallback(new MatchInitiatedCallback(this));
        }
    }

}
