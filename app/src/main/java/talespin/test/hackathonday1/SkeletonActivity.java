package talespin.test.hackathonday1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


public class SkeletonActivity extends BaseGameActivity {

    public static final String TAG = "DrawingActivity";

    // Local convenience pointers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*// Setup signin button
        findViewById(R.id.sign_out_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                        //setViewVisibility();
                    }
                });
*/
        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // start the asynchronous sign in flow
                        beginUserInitiatedSignIn();
                        //findViewById(R.id.sign_in_button).setVisibility(View.GONE);

                    }
                });


        /*
        mDataView = ((TextView) findViewById(R.id.data_view));
        mTurnTextView = ((TextView) findViewById(R.id.turn_counter_view));
        mStory = (TextView)findViewById(R.id.story_tillNow);
        mStoryTitle = (EditText) findViewById(R.id.story_title);
        mStoryTitleFixed =  (TextView)findViewById(R.id.story_title_fixed);
        mStory.setMovementMethod(new ScrollingMovementMethod());
        mListAllTurns = (ListView)findViewById(R.id.list_view_allTurn);

       // mListMyTurn = (ListView)findViewById(R.id.list_view_myTurn);
      //  mListTheirTurn = (ListView)findViewById(R.id.list_view_theirTurns);
      //  mListCompletedMatches = (ListView)findViewById(R.id.list_view_completed);
        mListInvitedMatches = (ListView)findViewById(R.id.list_view_invited);
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {
        Intent intent = new Intent(this, ListStories.class);
        startActivity(intent);
    }


}