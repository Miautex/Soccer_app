package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import pkgAdapter.BackgroundContainer;
import pkgAdapter.SwipeListAdapter;
import pkgAdapter.SwipeListAdapter2;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgData.Team;
import pkgListeners.OnOkDialogButtonPressedListener;

/**
 * @author Raphael Moser
 *
 */

public class TeamDivision2 extends BaseActivity implements View.OnClickListener, OnOkDialogButtonPressedListener {

    private SwipeListAdapter adapterAll;
    private SwipeListAdapter2 adapterTeam1, adapterTeam2;
    private ListView lvAllPlayers, lvTeam1, lvTeam2;
    private BackgroundContainer BackgroundContainerAll, BackgroundContainerTeam1, BackgroundContainerTeam2;
    private Button btnShuffle,btnShufflePos, btnContinue, btnCancel;
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    private HashMap<Long, Integer> mItemIdTopMapAll = new HashMap<>();
    private HashMap<Long, Integer> mItemIdTopMapTeam1 = new HashMap<>();
    private HashMap<Long, Integer> mItemIdTopMapTeam2 = new HashMap<>();
    private Direction direction = null;
    private Boolean isTouchTeamActive = false;
    private ArrayList<Player> players;
    private Game game;

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;

    private enum Direction {RIGHT, LEFT}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_division2);
        setTitle(R.string.title_activity_team_management);


        getIntents();
        getViews();
        createAdapters();
        setOnclickListenersAndAdapters();
    }

    private void getIntents(){
        game = (Game) this.getIntent().getSerializableExtra("game");
        players = (ArrayList<Player>) getIntent().getSerializableExtra("players");
    }

    private void getViews(){
        btnShuffle = (Button) findViewById(R.id.btnShuffle);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnShufflePos = (Button) findViewById(R.id.btnShufflePos);
        lvAllPlayers = (ListView) findViewById(R.id.lvAllPlayer);
        lvTeam1 = (ListView) findViewById(R.id.lvTeam1);
        lvTeam2 = (ListView) findViewById(R.id.lvTeam2);
        BackgroundContainerAll = (BackgroundContainer) findViewById(R.id.listViewBackgroundAllPlayers);
        BackgroundContainerTeam1 = (BackgroundContainer) findViewById(R.id.listViewBackgroundTeam1);
        BackgroundContainerTeam2 = (BackgroundContainer) findViewById(R.id.listViewBackgroundTeam2);
    }

    private void createAdapters(){
        adapterAll = new SwipeListAdapter(this, players, mTouchListenerAll);
        adapterAll.setColor(Color.WHITE);

        adapterTeam1 = new SwipeListAdapter2(this, new ArrayList<Player>(), mTouchListenerTeam1, Team.TEAM1);
        adapterTeam1.setColor(Color.GRAY);

        adapterTeam2 = new SwipeListAdapter2(this, new ArrayList<Player>(), mTouchListenerTeam2,Team.TEAM2);
        adapterTeam2.setColor(Color.GRAY);
    }

    private void setOnclickListenersAndAdapters(){
        lvTeam1.setAdapter(adapterTeam1);
        lvTeam2.setAdapter(adapterTeam2);
        lvAllPlayers.setAdapter(adapterAll);
        btnShufflePos.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnShuffle) {
                shufflePlayers();
                createWarning(getString(R.string.msg_ShuffleWarning));
            }
            else if (v.getId() == R.id.btnContinue) {
                ArrayList<Participation> participations = createParticipations();
                game.removeAllParticipations();
                for (Participation p : participations) {
                    game.addParticipation(p);
                }
                Intent myIntent = new Intent(this, AddGameEnterDataActivity.class);
                myIntent.putExtra("game", game);
                this.startActivity(myIntent);
            } else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
            else if(v.getId() ==  R.id.btnShufflePos){
                createWarning(getString(R.string.msg_ShuffleWarning));
                adapterTeam1.shufflePositions();
                adapterTeam1.notifyDataSetChanged();
                adapterTeam2.shufflePositions();
                adapterTeam2.notifyDataSetChanged();
            }
        }
        catch (Exception e){
            showMessage(e.getMessage());
        }
    }



    public ArrayList<Participation> createParticipations() throws Exception {
        ArrayList<Participation> list1 = adapterTeam1.getParticipations();
        ArrayList<Participation> list2 = adapterTeam2.getParticipations();
        if (adapterAll.getCount() != players.size()) {
            throw new Exception(getString(R.string.msg_PlayerhasNoTeam));
        } else if (Math.abs((list1.size() - list2.size())) > 1) {
            throw new Exception(getString(R.string.msg_UnbalancedTeams));
        }
        list1.addAll(list2);
        return list1;
    }


    private void createWarning(String s){
        OkDialog dia = new OkDialog(this, s);
        dia.show();
    }

    @Override
    public void okDialogButtonPressed() {

    }

    private View.OnTouchListener mTouchListenerAll = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(TeamDivision2.this).getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    BackgroundContainerAll.showBackground(v.getTop(), v.getHeight());
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1);
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE: {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            lvAllPlayers.requestDisallowInterceptTouchEvent(true);
                            BackgroundContainerAll.showBackground(v.getTop(), v.getHeight());
                        }
                    }
                    if (mSwiping) {
                        v.setTranslationX((x - mDownX));
                        v.setAlpha(1 - deltaXAbs / v.getWidth());
                    }
                }
                break;
                case MotionEvent.ACTION_UP: {
                    BackgroundContainerAll.hideBackground();
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > v.getWidth() / 4) {
                            if (x < mDownX) {
                                direction = Direction.LEFT;
                            } else if (x > mDownX) {
                                direction = Direction.RIGHT;
                            }
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / v.getWidth();
                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = Math.abs((int) ((1 - fractionCovered) * SWIPE_DURATION));
                        lvAllPlayers.setEnabled(false);
                        v.animate().setDuration(duration).alpha(endAlpha).translationX(endX).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Restore animated values
                                v.setAlpha(1);
                                v.setTranslationX(0);
                                if (remove) {
                                    animateRemovalAll(lvAllPlayers, v);
                                } else {
                                    BackgroundContainerAll.hideBackground();
                                    mSwiping = false;
                                    lvAllPlayers.setEnabled(true);
                                }
                            }
                        });
                    }
                }
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    private View.OnTouchListener mTouchListenerTeam1 = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (isTouchTeamActive){
                if (mSwipeSlop < 0) {
                    mSwipeSlop = ViewConfiguration.get(TeamDivision2.this).getScaledTouchSlop();
                }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    BackgroundContainerTeam1.showBackground(v.getTop(), v.getHeight());
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1);
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE: {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            lvTeam1.requestDisallowInterceptTouchEvent(true);
                            BackgroundContainerTeam1.showBackground(v.getTop(), v.getHeight());
                        }
                    }
                    if (mSwiping) {
                        v.setTranslationX((x - mDownX));
                        v.setAlpha(1 - deltaXAbs / v.getWidth());
                    }
                }
                break;
                case MotionEvent.ACTION_UP: {
                    BackgroundContainerTeam1.hideBackground();
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > v.getWidth() / 4) {
                            if (x < mDownX) {
                                direction = Direction.LEFT;
                            } else if (x > mDownX) {
                                direction = Direction.RIGHT;
                            }
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / v.getWidth();
                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = Math.abs((int) ((1 - fractionCovered) * SWIPE_DURATION));
                        lvTeam1.setEnabled(false);
                        v.animate().setDuration(duration).alpha(endAlpha).translationX(endX).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Restore animated values
                                v.setAlpha(1);
                                v.setTranslationX(0);
                                if (remove) {
                                    animateRemovalTeam1(lvTeam1, v);
                                } else {
                                    BackgroundContainerTeam1.hideBackground();
                                    mSwiping = false;
                                    lvTeam1.setEnabled(true);
                                }
                            }
                        });
                    }
                }
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
        return false;
        }
    };

    private View.OnTouchListener mTouchListenerTeam2 = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if(isTouchTeamActive) {
                if (mSwipeSlop < 0) {
                    mSwipeSlop = ViewConfiguration.get(TeamDivision2.this).getScaledTouchSlop();
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        BackgroundContainerTeam2.showBackground(v.getTop(), v.getHeight());
                        if (mItemPressed) {
                            // Multi-item swipes not handled
                            return false;
                        }
                        mItemPressed = true;
                        mDownX = event.getX();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.setAlpha(1);
                        v.setTranslationX(0);
                        mItemPressed = false;
                        break;
                    case MotionEvent.ACTION_MOVE: {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        if (!mSwiping) {
                            if (deltaXAbs > mSwipeSlop) {
                                mSwiping = true;
                                lvTeam2.requestDisallowInterceptTouchEvent(true);
                                BackgroundContainerTeam2.showBackground(v.getTop(), v.getHeight());
                            }
                        }
                        if (mSwiping) {
                            v.setTranslationX((x - mDownX));
                            v.setAlpha(1 - deltaXAbs / v.getWidth());
                        }
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        BackgroundContainerTeam2.hideBackground();
                        // User let go - figure out whether to animate the view out, or back into place
                        if (mSwiping) {
                            float x = event.getX() + v.getTranslationX();
                            float deltaX = x - mDownX;
                            float deltaXAbs = Math.abs(deltaX);
                            float fractionCovered;
                            float endX;
                            float endAlpha;
                            final boolean remove;
                            if (deltaXAbs > (v.getWidth() / 4)) {
                                if (x < mDownX) {
                                    direction = Direction.LEFT;
                                } else if (x > mDownX) {
                                    direction = Direction.RIGHT;
                                }
                                // Greater than a quarter of the width - animate it out
                                fractionCovered = deltaXAbs / v.getWidth();
                                endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                                endAlpha = 0;
                                remove = true;
                            } else {
                                // Not far enough - animate it back
                                fractionCovered = 1 - (deltaXAbs / v.getWidth());
                                endX = 0;
                                endAlpha = 1;
                                remove = false;
                            }
                            // Animate position and alpha of swiped item
                            // NOTE: This is a simplified version of swipe behavior, for the
                            // purposes of this demo about animation. A real version should use
                            // velocity (via the VelocityTracker class) to send the item off or
                            // back at an appropriate speed.
                            long duration = Math.abs((int) ((1 - fractionCovered) * SWIPE_DURATION));
                            lvTeam2.setEnabled(false);
                            v.animate().setDuration(duration).alpha(endAlpha).translationX(endX).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    // Restore animated values
                                    v.setAlpha(1);
                                    v.setTranslationX(0);
                                    if (remove) {
                                        animateRemovalTeam2(lvTeam2, v);
                                    } else {
                                        BackgroundContainerTeam2.hideBackground();
                                        mSwiping = false;
                                        lvTeam2.setEnabled(true);
                                    }
                                }
                            });
                        }
                    }
                    mItemPressed = false;
                    break;
                    default:
                        return false;
                }
                return true;
            }
            return false;
        }
    };

    private void movePlayerFromAll(View viewToMove) {
        int position = lvAllPlayers.getPositionForView(viewToMove);
        if (direction == Direction.RIGHT) {
            adapterTeam2.add(adapterAll.getItem(position));
            adapterTeam2.notifyDataSetChanged();
            adapterAll.remove(adapterAll.getItem(position));
        } else if (direction == Direction.LEFT) {
            adapterTeam1.add(adapterAll.getItem(position));
            adapterTeam1.notifyDataSetChanged();
            adapterAll.remove(adapterAll.getItem(position));
        }
        if (adapterAll.getCount() == 0) {
            BackgroundContainerAll.setVisibility(View.INVISIBLE);
            lvAllPlayers.setVisibility(View.INVISIBLE);
            adapterTeam1.setColor(Color.WHITE);
            adapterTeam1.notifyDataSetChanged();
            adapterTeam2.setColor(Color.WHITE);
            adapterTeam2.notifyDataSetChanged();
            isTouchTeamActive = true;
        }

    }

    private void movePlayerFromTeam1(View viewToMove) {
        int position = lvTeam1.getPositionForView(viewToMove);
        if (direction == Direction.RIGHT) {
            adapterTeam2.addWithPosition(adapterTeam1.removeWithPos(adapterTeam1.getItem(position)));
            adapterTeam2.notifyDataSetChanged();
        }
        else{
            adapterTeam1.addWithPosition(adapterTeam1.removeWithPos(adapterTeam1.getItem(position)));
            adapterTeam1.notifyDataSetChanged();
        }
    }

    private void movePlayerFromTeam2(View viewToMove) {
        int position = lvTeam2.getPositionForView(viewToMove);
        if (direction == Direction.LEFT) {
            adapterTeam1.addWithPosition(adapterTeam2.removeWithPos(adapterTeam2.getItem(position)));
            adapterTeam1.notifyDataSetChanged();
        }
        else{
            adapterTeam2.addWithPosition(adapterTeam2.removeWithPos(adapterTeam2.getItem(position)));
            adapterTeam2.notifyDataSetChanged();
        }
    }

    private void shufflePlayers(){
        Random rand = new Random();
        ArrayList<Player> freePlayers = adapterAll.getFreePlayers();
        int diff = adapterTeam1.getCount() - adapterTeam2.getCount();
        if (freePlayers.size() != 0) {
            if (diff < 0) {
                for (int i = 0; i > diff; diff++) {
                    Player p = adapterAll.getItem(rand.nextInt(freePlayers.size()));
                    adapterTeam1.add(p);
                    adapterAll.remove(p);
                }
            } else if (diff > 0) {
                for (int i = 0; i < diff; diff--) {
                    Player p = adapterAll.getItem(rand.nextInt(freePlayers.size()));
                    adapterTeam2.add(p);
                    adapterAll.remove(p);
                }
            }
            if (diff == 0 && freePlayers.size() != 0) {
                for (int i = 0; i < freePlayers.size(); ) {
                    if (diff == 0) {
                        Player p = adapterAll.getItem(rand.nextInt(freePlayers.size()));
                        adapterTeam1.add(p);
                        adapterAll.remove(p);
                        diff++;
                    } else {
                        Player p = adapterAll.getItem(rand.nextInt(freePlayers.size()));
                        adapterTeam2.add(p);
                        adapterAll.remove(p);
                        diff--;
                    }
                }
            }
            if (adapterAll.getCount() == 0) {
                BackgroundContainerAll.setVisibility(View.INVISIBLE);
                lvAllPlayers.setVisibility(View.INVISIBLE);
                adapterTeam1.setColor(Color.WHITE);
                adapterTeam1.notifyDataSetChanged();
                adapterTeam2.setColor(Color.WHITE);
                adapterTeam2.notifyDataSetChanged();
                isTouchTeamActive = true;
            }
        } else {
            showMessage(getString(R.string.msg_NoPlayersToAssign));
        }

    }

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void animateRemovalAll(final ListView listview, View viewToRemove) {
        final View viewToRemove2 = viewToRemove;
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove2) {
                int position = firstVisiblePosition + i;
                long itemId = adapterAll.getItemId(position);
                mItemIdTopMapAll.put(itemId, child.getTop());
            }
        }
        // Moves the Item to Team
        //movePlayerFromAll(viewToRemove);

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapterAll.getItemId(position);
                    Integer startTop = mItemIdTopMapAll.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        BackgroundContainerAll.hideBackground();
                                        mSwiping = false;
                                        lvAllPlayers.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                                movePlayerFromAll(viewToRemove2);
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    BackgroundContainerAll.hideBackground();
                                    mSwiping = false;
                                    lvAllPlayers.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                            movePlayerFromAll(viewToRemove2);
                        }
                    }
                }
                mItemIdTopMapAll.clear();
                BackgroundContainerAll.hideBackground();
                return true;
            }
        });
    }

    private void animateRemovalTeam1(final ListView listview, View viewToRemove) {
        final View viewToRemove2 = viewToRemove;
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = adapterTeam1.getItemId(position);
                mItemIdTopMapTeam1.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        //movePlayerFromTeam1(viewToRemove);

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapterTeam1.getItemId(position);
                    Integer startTop = mItemIdTopMapTeam1.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        BackgroundContainerTeam1.hideBackground();
                                        mSwiping = false;
                                        lvTeam1.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                                movePlayerFromTeam1(viewToRemove2);
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    BackgroundContainerTeam1.hideBackground();
                                    mSwiping = false;
                                    lvTeam1.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                            movePlayerFromTeam1(viewToRemove2);
                        }
                    }
                }
                mItemIdTopMapTeam1.clear();
                BackgroundContainerTeam1.hideBackground();
                return true;
            }
        });
    }

    private void animateRemovalTeam2(final ListView listview, View viewToRemove) {
        final View viewToRemove2 = viewToRemove;
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = adapterTeam2.getItemId(position);
                mItemIdTopMapTeam2.put(itemId, child.getTop());
            }
        }
        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapterTeam2.getItemId(position);
                    Integer startTop = mItemIdTopMapTeam2.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        BackgroundContainerTeam2.hideBackground();
                                        mSwiping = false;
                                        lvTeam2.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                                movePlayerFromTeam2(viewToRemove2);
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    BackgroundContainerTeam2.hideBackground();
                                    mSwiping = false;
                                    lvTeam2.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                            movePlayerFromTeam2(viewToRemove2);
                        }
                    }
                }
                mItemIdTopMapTeam2.clear();
                BackgroundContainerTeam2.hideBackground();
                return true;
            }
        });
    }
}
