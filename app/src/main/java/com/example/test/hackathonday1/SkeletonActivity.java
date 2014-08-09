package com.example.test.hackathonday1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationBuffer;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchBuffer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class SkeletonActivity extends BaseGameActivity implements OnInvitationReceivedListener, OnTurnBasedMatchUpdateReceivedListener {

    public static final String TAG = "DrawingActivity";

    // Local convenience pointers
    public TextView mDataView;
    public TextView mTurnTextView;
    public TextView mStory;
    public TextView mStoryTitle;
    public TextView mStoryTitleFixed;
    private AlertDialog mAlertDialog;

    private ListView mListAllTurns;

    private ListView mListMyTurn;
    private ListView mListTheirTurn;
    private ListView mListCompletedMatches;
    private ListView mListInvitedMatches;

    // For our intents
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_LOOK_AT_MATCHES = 10001;

    // How long to show toasts.
    final static int TOAST_DELAY = 2000;

    // Should I be showing the turn API?
    public boolean isDoingTurn = false;

    // This is the current match we're in; null if not loaded
    public TurnBasedMatch mMatch;

    // This is the current match data after being unpersisted.
    // Do not retain references to match data once you have
    // taken an action on the match, such as takeTurn()
    public SkeletonTurn mTurnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup signin button
        findViewById(R.id.sign_out_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                        setViewVisibility();
                    }
                });

        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // start the asynchronous sign in flow
                        beginUserInitiatedSignIn();

                        findViewById(R.id.sign_in_button).setVisibility(
                                View.GONE);

                    }
                });
        mDataView = ((TextView) findViewById(R.id.data_view));
        mTurnTextView = ((TextView) findViewById(R.id.turn_counter_view));
        mStory = (TextView)findViewById(R.id.story_tillNow);
        mStoryTitle = (EditText) findViewById(R.id.story_title);
        mStoryTitleFixed =  (TextView)findViewById(R.id.story_title_fixed);

        mListAllTurns = (ListView)findViewById(R.id.list_view_allTurn);

       // mListMyTurn = (ListView)findViewById(R.id.list_view_myTurn);
      //  mListTheirTurn = (ListView)findViewById(R.id.list_view_theirTurns);
      //  mListCompletedMatches = (ListView)findViewById(R.id.list_view_completed);
        mListInvitedMatches = (ListView)findViewById(R.id.list_view_invited);
    }

    // Displays your inbox. You will get back onActivityResult where
    // you will need to figure out what you clicked on.
    public void onCheckGamesClicked(View view) {
        /*Games.Invitations.loadInvitations(getApiClient()).setResultCallback(new ResultCallback<Invitations.LoadInvitationsResult>() {
            @Override
            public void onResult(Invitations.LoadInvitationsResult result) {
                result.getInvitations();
            }
        });*/

   /*     Games.TurnBasedMultiplayer.loadMatchesByStatus(getApiClient(), new int[]{TurnBasedMatch.MATCH_STATUS_ACTIVE, TurnBasedMatch.MATCH_STATUS_COMPLETE, TurnBasedMatch.MATCH_TURN_STATUS_INVITED}).
                setResultCallback(new ResultCallback<TurnBasedMultiplayer.LoadMatchesResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.LoadMatchesResult result) {
                        //result.getMatches();
                        showAvailableGames(result);
                    }
                });
*/
        Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(getApiClient());
        startActivityForResult(intent, RC_LOOK_AT_MATCHES);
    }

    public void ClickCheckGames()
    {
        Games.TurnBasedMultiplayer.loadMatchesByStatus(getApiClient(), new int[]{TurnBasedMatch.MATCH_STATUS_ACTIVE, TurnBasedMatch.MATCH_STATUS_COMPLETE, TurnBasedMatch.MATCH_TURN_STATUS_INVITED}).
                setResultCallback(new ResultCallback<TurnBasedMultiplayer.LoadMatchesResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.LoadMatchesResult result) {
                        //result.getMatches();
                        showAvailableGames(result);
                    }
                });
    }
    public void showAvailableGames(TurnBasedMultiplayer.LoadMatchesResult result)
    {
        findViewById(R.id.login_layout).setVisibility(View.GONE);
        findViewById(R.id.gameplay_layout).setVisibility(View.GONE);
        findViewById(R.id.matchup_layout).setVisibility(View.GONE);

        findViewById(R.id.game_list_layout).setVisibility(View.VISIBLE);
        //findViewById(R.id.invitedMatches).setVisibility(View.VISIBLE);
        findViewById(R.id.allTurns).setVisibility(View.VISIBLE);

        //findViewById(R.id.invitedMatches).setVisibility(View.VISIBLE);
        //findViewById(R.id.myTurns).setVisibility(View.VISIBLE);
        //findViewById(R.id.theirTurns).setVisibility(View.VISIBLE);
        //findViewById(R.id.completedMatches).setVisibility(View.VISIBLE);

        LoadMatchesResponse matches = result.getMatches();
        //TurnBasedMatchBuffer myTurnMatches = matches.getMyTurnMatches();
        //TurnBasedMatchBuffer theirTurnMatches = matches.getTheirTurnMatches();
        //TurnBasedMatchBuffer completedMatches = matches.getCompletedMatches();
        //InvitationBuffer invitations = matches.getInvitations();


        DisplayXMatchesData(matches, mListInvitedMatches);

       /* DisplayXMatchesData(invitations, mListInvitedMatches);

        DisplayXMatchesData(myTurnMatches, mListMyTurn);

        DisplayXMatchesData(theirTurnMatches, mListTheirTurn);

        DisplayXMatchesData(completedMatches, mListCompletedMatches);*/


    }

    private void DisplayXMatchesData(final LoadMatchesResponse matchResponse, final ListView mList)
    {
        if ( matchResponse == null ) return;
        final ArrayList<SingleListItem> myList = new ArrayList<SingleListItem>();

        final InvitationBuffer invitations = matchResponse.getInvitations();
        if(invitations != null)
        {
            for(int i=0; i<invitations.getCount() ; i++)
            {
                String story_Title = invitations.get(i).getInviter().getDisplayName() + " has invited you";
                long wait_time = System.currentTimeMillis() - invitations.get(i).getCreationTimestamp();
                wait_time = wait_time / 1000;
                myList.add(new SingleListItem(story_Title, wait_time));
            }
        }

        final TurnBasedMatchBuffer myTurnMatches = matchResponse.getMyTurnMatches();
        if ( myTurnMatches != null )
        {
            for ( int i = 0; i < myTurnMatches.getCount(); i++ )
            {
                String story_Title = SkeletonTurn.unpersist(myTurnMatches.get(i).getData()).storyTitle;
                long wait_time = System.currentTimeMillis() - myTurnMatches.get(i).getLastUpdatedTimestamp();
                wait_time = wait_time / 1000;
                myList.add(new SingleListItem(story_Title, wait_time));
            }
        }

        final TurnBasedMatchBuffer yourTurnMatches = matchResponse.getTheirTurnMatches();
        if ( yourTurnMatches != null )
        {
            for ( int i = 0; i < yourTurnMatches.getCount(); i++ )
            {
                String story_Title = SkeletonTurn.unpersist(yourTurnMatches.get(i).getData()).storyTitle;
                long wait_time = System.currentTimeMillis() - yourTurnMatches.get(i).getLastUpdatedTimestamp();
                wait_time = wait_time / 1000;
                myList.add(new SingleListItem(story_Title, wait_time));
            }
        }

        final TurnBasedMatchBuffer completedTurnMatches = matchResponse.getCompletedMatches();
        if ( completedTurnMatches != null )
        {
            for ( int i = 0; i < completedTurnMatches.getCount(); i++ )
            {
                String story_Title = SkeletonTurn.unpersist(completedTurnMatches.get(i).getData()).storyTitle;
                long wait_time = System.currentTimeMillis() - completedTurnMatches.get(i).getLastUpdatedTimestamp();
                wait_time = wait_time / 1000;
                myList.add(new SingleListItem(story_Title, wait_time));
            }
        }

        SingleListItemAdapter adapter = new SingleListItemAdapter(this, myList);
        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Show Alert
                if ( invitations != null )
                {
                    if ( position >= 0 && position < invitations.getCount() )
                    {

                    }
                }
                if( myTurnMatches != null )
                {
                    if ( position >= 0 && position < myTurnMatches.getCount() )
                    {
                        updateMatch(myTurnMatches.get(position));
                    }
                    else {
                        position -=  myTurnMatches.getCount();

                        if ( yourTurnMatches != null )
                        {
                            if ( position >= 0 && position < yourTurnMatches.getCount() )
                            {
                                updateMatch(yourTurnMatches.get(position));
                            }
                            else {
                                position -= yourTurnMatches.getCount();

                                if ( completedTurnMatches != null )
                                {
                                    if ( position >= 0 && position < completedTurnMatches.getCount() )
                                    {
                                        updateMatch(completedTurnMatches.get(position));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        });

    }

    /*private void DisplayXMatchesData(final TurnBasedMatchBuffer matches, final ListView mList) {
        final ArrayList<SingleListItem> list = new ArrayList<SingleListItem>();
        if(matches == null)
        {
            return;
        }

        for(int i=0; i<matches.getCount() ; i++)
        {
            String story_Title = SkeletonTurn.unpersist(matches.get(i).getData()).storyTitle;
            long wait_time = System.currentTimeMillis() - matches.get(i).getLastUpdatedTimestamp();
            wait_time = wait_time / 1000;
            list.add(new SingleListItem(story_Title, wait_time));
        }

        SingleListItemAdapter adapter = new SingleListItemAdapter(this, list);

 //       mList.addHeaderView(textView);

        mList.setAdapter(adapter);



        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                SingleListItem  itemValue    = (SingleListItem) mList.getItemAtPosition(position);

                // Show Alert
                updateMatch(matches.get(position));
            }

        });
    }*/


    /*private void DisplayXMatchesData(final InvitationBuffer invitations, final ListView mList) {
        final ArrayList<SingleListItem> list = new ArrayList<SingleListItem>();
        if(invitations == null)
        {
            return;
        }

        for(int i=0; i<invitations.getCount() ; i++)
        {
            String story_Title = invitations.get(i).getInviter().getDisplayName() + "has invited you";
            long wait_time = System.currentTimeMillis() - invitations.get(i).getCreationTimestamp();
            wait_time = wait_time / 1000;
            list.add(new SingleListItem(story_Title, wait_time));
        }

        SingleListItemAdapter adapter = new SingleListItemAdapter(this, list);

        //       mList.addHeaderView(textView);

        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                SingleListItem  itemValue    = (SingleListItem) mList.getItemAtPosition(position);

                // Show Alert
                //updateMatch(invitations.get(position));
            }

        });
    }*/


    // Open the create-game UI. You will get back an onActivityResult
    // and figure out what to do.
    public void onStartMatchClicked(View view) {
                Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(getApiClient(),
                1, 7, false);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    public void ClickStartMatchButton()
    {
        Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(getApiClient(),
                1, 7, false);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    // Create a one-on-one automatch game.
    public void onQuickMatchClicked(View view) {

        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                1, 1, 0);

        TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                .setAutoMatchCriteria(autoMatchCriteria).build();

        showSpinner();

        // Start the match
        ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> cb = new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
            @Override
            public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                processResult(result);
            }
        };
        Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc).setResultCallback(cb);
    }

    // In-game controls

    // Cancel the game. Should possibly wait until the game is canceled before
    // giving up on the view.
    public void onCancelClicked(View view) {
        showSpinner();
        Games.TurnBasedMultiplayer.cancelMatch(getApiClient(), mMatch.getMatchId())
                .setResultCallback(new ResultCallback<TurnBasedMultiplayer.CancelMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.CancelMatchResult result) {
                        processResult(result);
                    }
                });
        isDoingTurn = false;
        setViewVisibility();
    }

    // Leave the game during your turn. Note that there is a separate
    // Games.TurnBasedMultiplayer.leaveMatch() if you want to leave NOT on your turn.
    public void onLeaveClicked(View view) {
        showSpinner();
        String nextParticipantId = getNextParticipantId();

        Games.TurnBasedMultiplayer.leaveMatchDuringTurn(getApiClient(), mMatch.getMatchId(),
                nextParticipantId).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.LeaveMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.LeaveMatchResult result) {
                        processResult(result);
                    }
                });
        setViewVisibility();
    }

    // Finish the game. Sometimes, this is your only choice.
    public void onFinishClicked(View view) {
        showSpinner();
        Games.TurnBasedMultiplayer.finishMatch(getApiClient(), mMatch.getMatchId())
                .setResultCallback(new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                        processResult(result);
                    }
                });

        isDoingTurn = false;
        setViewVisibility();
    }


    // Upload your new gamestate, then take a turn, and pass it on to the next
    // player.
    public void onDoneClicked(View view) {
        showSpinner();

        String nextParticipantId = getNextParticipantId();
        String title;
        if(mStoryTitle.getText().length() != 0)
        {
            title = mStoryTitle.getText().toString();
            mTurnData.storyTitle = title;
        }
        else
        {
            title = mStoryTitleFixed.getText().toString();
        }


        // Create the next turn
        mTurnData.turnCounter += 1;

        mTurnData.data = mTurnData.data + mDataView.getText().toString();

        showSpinner();

        Games.TurnBasedMultiplayer.takeTurn(getApiClient(), mMatch.getMatchId(),
                mTurnData.persist(), nextParticipantId).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                        processResult(result);
                    }
                });

        mTurnData = null;
    }

    // Sign-in, Sign out behavior

    // Update the visibility based on what state we're in.
    public void setViewVisibility() {
        if (!isSignedIn()) {
            findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.matchup_layout).setVisibility(View.GONE);
            findViewById(R.id.gameplay_layout).setVisibility(View.GONE);

            if (mAlertDialog != null) {
                mAlertDialog.dismiss();
            }
            return;
        }


        ((TextView) findViewById(R.id.name_field)).setText(Games.Players.getCurrentPlayer(
                getApiClient()).getDisplayName());
        findViewById(R.id.login_layout).setVisibility(View.GONE);

        if (isDoingTurn) {
           // findViewById(R.id.game_list_layout).setVisibility(View.GONE);
            findViewById(R.id.matchup_layout).setVisibility(View.GONE);
            findViewById(R.id.gameplay_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.matchup_layout).setVisibility(View.VISIBLE);
           // ClickCheckGames();
            findViewById(R.id.gameplay_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSignInFailed() {
        setViewVisibility();
    }

    @Override
    public void onSignInSucceeded() {
        String temp = getInvitationId();

        if (mHelper.getTurnBasedMatch() != null) {
            // GameHelper will cache any connection hint it gets. In this case,
            // it can cache a TurnBasedMatch that it got from choosing a turn-based
            // game notification. If that's the case, you should go straight into
            // the game.
            updateMatch(mHelper.getTurnBasedMatch());
            return;
        }

        setViewVisibility();

        // As a demonstration, we are registering this activity as a handler for
        // invitation and match events.

        // This is *NOT* required; if you do not register a handler for
        // invitation events, you will get standard notifications instead.
        // Standard notifications may be preferable behavior in many cases.
        Games.Invitations.registerInvitationListener(getApiClient(), this);

        // Likewise, we are registering the optional MatchUpdateListener, which
        // will replace notifications you would get otherwise. You do *NOT* have
        // to register a MatchUpdateListener.
        Games.TurnBasedMultiplayer.registerMatchUpdateListener(getApiClient(), this);
    }

    // Switch to gameplay view.
    public void setGameplayUI() {
        isDoingTurn = true;
        setViewVisibility();
        mDataView.setText("");
        mTurnTextView.setText("Turn " + mTurnData.turnCounter);

        if(mTurnData.turnCounter == 0)
        {
            mStoryTitle.setVisibility(View.VISIBLE);
            return;
        }
        //this.setTitle(mTurnData.storyTitle);
        //findViewById(R.string.app_name).setVisibility(View.GONE);

        mStoryTitle.setVisibility(View.GONE);
        mStoryTitleFixed.setText(mTurnData.storyTitle);
        mStory.setText(mTurnData.data);
    }

    // Helpful dialogs

    public void showSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
    }

    public void dismissSpinner() {
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }

    // Generic warning/info dialog
    public void showWarning(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title).setMessage(message);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                    }
                });

        // create alert dialog
        mAlertDialog = alertDialogBuilder.create();

        // show it
        mAlertDialog.show();
    }

    // Rematch dialog
    public void askForRematch() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Do you want a rematch?");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Sure, rematch!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                rematch();
                            }
                        })
                .setNegativeButton("No.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

        alertDialogBuilder.show();
    }

    // This function is what gets called when you return from either the Play
    // Games built-in inbox, or else the create game built-in interface.
    @Override
    public void onActivityResult(int request, int response, Intent data) {
        // It's VERY IMPORTANT for you to remember to call your superclass.
        // BaseGameActivity will not work otherwise.
        super.onActivityResult(request, response, data);

        if (request == RC_LOOK_AT_MATCHES) {
            // Returning from the 'Select Match' dialog

            if (response != Activity.RESULT_OK) {
                // user canceled
                return;
            }

            TurnBasedMatch match = data
                    .getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);

            if (match != null) {
                updateMatch(match);
            }

            Log.d(TAG, "Match = " + match);
        } else if (request == RC_SELECT_PLAYERS) {
            // Returned from 'Select players to Invite' dialog

            if (response != Activity.RESULT_OK) {
                // user canceled
                return;
            }


            // get the invitee list
            final ArrayList<String> invitees = data
                    .getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            // get automatch criteria
            Bundle autoMatchCriteria = null;

            int minAutoMatchPlayers = data.getIntExtra(
                    Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers = data.getIntExtra(
                    Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            if (minAutoMatchPlayers > 0) {
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            } else {
                autoMatchCriteria = null;
            }

            TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                    .addInvitedPlayers(invitees)
                    .setAutoMatchCriteria(autoMatchCriteria).build();

            // Start the match
            Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc).setResultCallback(
                    new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
                        @Override
                        public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                            processResult(result);
                        }
                    });
            showSpinner();
        }
    }

    // startMatch() happens in response to the createTurnBasedMatch()
    // above. This is only called on success, so we should have a
    // valid match object. We're taking this opportunity to setup the
    // game, saving our initial state. Calling takeTurn() will
    // callback to OnTurnBasedMatchUpdated(), which will show the game
    // UI.
    public void startMatch(TurnBasedMatch match) {
        mStoryTitle.setVisibility(View.VISIBLE);
        mTurnData = new SkeletonTurn();
        // Some basic turn data
        mTurnData.data = "";
        mStoryTitle.setText("");
        mStory.setText("");
        mStoryTitleFixed.setText("");
        mTurnData.turnCounter = 0;

        mMatch = match;

        String playerId = Games.Players.getCurrentPlayerId(getApiClient());
        String myParticipantId = mMatch.getParticipantId(playerId);

        showSpinner();

        Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
                mTurnData.persist(), myParticipantId).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                        processResult(result);
                    }
                });
    }

    // If you choose to rematch, then call it and wait for a response.
    public void rematch() {
        showSpinner();
        Games.TurnBasedMultiplayer.rematch(getApiClient(), mMatch.getMatchId()).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                        processResult(result);
                    }
                });
        mMatch = null;
        isDoingTurn = false;
    }

    /**
     * Get the next participant. In this function, we assume that we are
     * round-robin, with all known players going before all automatch players.
     * This is not a requirement; players can go in any order. However, you can
     * take turns in any order.
     *
     * @return participantId of next player, or null if automatching
     */
    public String getNextParticipantId() {

        String playerId = Games.Players.getCurrentPlayerId(getApiClient());
        String myParticipantId = mMatch.getParticipantId(playerId);

        ArrayList<String> participantIds = mMatch.getParticipantIds();

        int desiredIndex = -1;

        for (int i = 0; i < participantIds.size(); i++) {
            if (participantIds.get(i).equals(myParticipantId)) {
                desiredIndex = i + 1;
            }
        }

        if (desiredIndex < participantIds.size()) {
            return participantIds.get(desiredIndex);
        }

        if (mMatch.getAvailableAutoMatchSlots() <= 0) {
            // You've run out of automatch slots, so we start over.
            return participantIds.get(0);
        } else {
            // You have not yet fully automatched, so null will find a new
            // person to play against.
            return null;
        }
    }

    // This is the main function that gets called when players choose a match
    // from the inbox, or else create a match and want to start it.
    public void updateMatch(TurnBasedMatch match) {
        mMatch = match;

        int status = match.getStatus();
        int turnStatus = match.getTurnStatus();

        switch (status) {
            case TurnBasedMatch.MATCH_STATUS_CANCELED:
                showWarning("Canceled!", "This game was canceled!");
                return;
            case TurnBasedMatch.MATCH_STATUS_EXPIRED:
                showWarning("Expired!", "This game is expired.  So sad!");
                return;
            case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
                showWarning("Waiting for auto-match...",
                        "We're still waiting for an automatch partner.");
                return;
            case TurnBasedMatch.MATCH_STATUS_COMPLETE:
                if (turnStatus == TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
                    showWarning(
                            "Complete!",
                            "This game is over; someone finished it, and so did you!  There is nothing to be done.");
                    break;
                }

                // Note that in this state, you must still call "Finish" yourself,
                // so we allow this to continue.
                showWarning("Complete!",
                        "This game is over; someone finished it!  You can only finish it now.");
        }

        // OK, it's active. Check on turn status.
        switch (turnStatus) {
            case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
                mTurnData = SkeletonTurn.unpersist(mMatch.getData());
                setGameplayUI();
                return;
            case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
                // Should return results.
                showWarning(SkeletonTurn.unpersist(match.getData()).storyTitle,  SkeletonTurn.unpersist(match.getData()).data);
                break;
            case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
                showWarning("Good inititative!",
                        "Still waiting for invitations.\n\nBe patient!");
        }

        mTurnData = null;

        setViewVisibility();
    }

    private void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
        dismissSpinner();

        if (!checkStatusCode(null, result.getStatus().getStatusCode())) {
            return;
        }

        isDoingTurn = false;

        showWarning("Match",
                "This match is canceled.  All other players will have their game ended.");
    }

    private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();

        if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
            return;
        }

        if (match.getData() != null) {
            // This is a game that has already started, so I'll just start
            updateMatch(match);
            return;
        }

        startMatch(match);
    }


    private void processResult(TurnBasedMultiplayer.LeaveMatchResult result) {
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
            return;
        }
        isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);
        showWarning("Left", "You've left this match.");
    }


    public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
            return;
        }


        if (match.canRematch()) {
            askForRematch();
        }

        isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

        if (isDoingTurn) {
            updateMatch(match);
            return;
        }

        setViewVisibility();
    }

    // Handle notification events.
    @Override
    public void onInvitationReceived(Invitation invitation) {

        Toast.makeText(
                this,
                "An invitation has arrived from "
                        + invitation.getInviter().getDisplayName(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onInvitationRemoved(String invitationId) {
        Toast.makeText(this, "An invitation was removed.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch match) {
        Toast.makeText(this, "A match was updated.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTurnBasedMatchRemoved(String matchId) {
        Toast.makeText(this, "A match was removed.", Toast.LENGTH_LONG).show();

    }

    public void showErrorMessage(TurnBasedMatch match, int statusCode,
                                 int stringId) {

        showWarning("Warning", getResources().getString(stringId));
    }

    // Returns false if something went wrong, probably. This should handle
    // more cases, and probably report more accurate results.
    private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
        switch (statusCode) {
            case GamesStatusCodes.STATUS_OK:
                return true;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
                // This is OK; the action is stored by Google Play Services and will
                // be dealt with later.
                /*Toast.makeText(
                        this,
                        "Stored action for later.  (Please remove this toast before release.)",
                        TOAST_DELAY).show();
                */// NOTE: This toast is for informative reasons only; please remove
                // it from your final application.
                return true;
            case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                showErrorMessage(match, statusCode,
                        R.string.status_multiplayer_error_not_trusted_tester);
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
                showErrorMessage(match, statusCode,
                        R.string.match_error_already_rematched);
                break;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_FAILED:
                showErrorMessage(match, statusCode,
                        R.string.network_error_operation_failed);
                break;
            case GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
                showErrorMessage(match, statusCode,
                        R.string.client_reconnect_required);
                break;
            case GamesStatusCodes.STATUS_INTERNAL_ERROR:
                showErrorMessage(match, statusCode, R.string.internal_error);
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
                showErrorMessage(match, statusCode,
                        R.string.match_error_inactive_match);
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
                showErrorMessage(match, statusCode,
                        R.string.match_error_locally_modified);
                break;
            default:
                showErrorMessage(match, statusCode, R.string.unexpected_status);
                Log.d(TAG, "Did not have warning or string to deal with: "
                        + statusCode);
        }

        return false;
    }


}
