package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import pkgAdapter.StableArrayAdapter;

public class TeamDivision2 extends BaseActivity /*implements View.OnTouchListener*/ {

    StableArrayAdapter adapterAll, adapterTeam1, adapterTeam2;
    ListView lvAllPlayers, lvTeam1, lvTeam2;
    BackgroundContainer BackgroundContainerAll, BackgroundContainerTeam1, BackgroundContainerTeam2;
    boolean mSwiping = false;
    boolean mItemPressed = false;
    HashMap<Long, Integer> mItemIdTopMapAll = new HashMap<Long, Integer>();
    HashMap<Long, Integer> mItemIdTopMapTeam1 = new HashMap<Long, Integer>();
    HashMap<Long, Integer> mItemIdTopMapTeam2 = new HashMap<Long, Integer>();
    Direction direction = null;

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;

    private enum Direction {RIGHT, LEFT}

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_division2);
        ArrayList<String> allPlayers = new ArrayList<>();
        allPlayers.add("a");
        allPlayers.add("b");
        allPlayers.add("c");
        allPlayers.add("d");
        allPlayers.add("e");
        allPlayers.add("f");

        ArrayList<String> team1 = new ArrayList<>();
        ArrayList<String> team2 = new ArrayList<>();

        BackgroundContainerAll = (BackgroundContainer) findViewById(R.id.listViewBackgroundAllPlayers);
        BackgroundContainerTeam1 = (BackgroundContainer) findViewById(R.id.listViewBackgroundTeam1);
        BackgroundContainerTeam2 = (BackgroundContainer) findViewById(R.id.listViewBackgroundTeam2);

        adapterAll = new StableArrayAdapter(this, allPlayers, mTouchListenerAll);
        adapterTeam1 = new StableArrayAdapter(this, team1, mTouchListenerTeam1);
        adapterTeam2 = new StableArrayAdapter(this, team2, mTouchListenerTeam2);

        // adapterTeam2 = new StableArrayAdapter(this, R.layout.opaque_text_view, team2, mTouchListenerTeam2);

        lvAllPlayers = (ListView) findViewById(R.id.lvAllPlayer);
        lvTeam1 = (ListView) findViewById(R.id.lvTeam1);
        lvTeam2 = (ListView) findViewById(R.id.lvTeam2);

        lvAllPlayers.setAdapter(adapterAll);
        lvTeam1.setAdapter(adapterTeam1);
        lvTeam2.setAdapter(adapterTeam2);

        lvAllPlayers.setAdapter(adapterAll);
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
                        if (deltaXAbs > v.getWidth() / 2) {
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
                        if (deltaXAbs > v.getWidth() / 10) {
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
    };

    private View.OnTouchListener mTouchListenerTeam2 = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
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
                        if (deltaXAbs > (v.getWidth() / 100)) {
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
        }

    }

    private void movePlayerFromTeam1(View viewToMove) {
        int position = lvTeam1.getPositionForView(viewToMove);
        if (direction == Direction.RIGHT) {
            adapterTeam2.add(adapterTeam1.getItem(position));
            adapterTeam2.notifyDataSetChanged();
            adapterTeam1.remove(adapterTeam1.getItem(position));
        } else if (direction == Direction.LEFT) {
            adapterAll.add(adapterTeam1.getItem(position));
            adapterAll.notifyDataSetChanged();
            adapterTeam1.remove(adapterTeam1.getItem(position));
        }

    }

    private void movePlayerFromTeam2(View viewToMove) {
        int position = lvTeam2.getPositionForView(viewToMove);
        if (direction == Direction.LEFT) {
            adapterTeam1.add(adapterTeam2.getItem(position));
            adapterTeam1.notifyDataSetChanged();
            adapterTeam2.remove(adapterTeam2.getItem(position));
        } else if (direction == Direction.RIGHT) {
            adapterAll.add(adapterTeam2.getItem(position));
            adapterAll.notifyDataSetChanged();
            adapterTeam2.remove(adapterTeam2.getItem(position));
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
        // Delete the item from the adapter
        //movePlayerFromTeam2(viewToRemove);

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
