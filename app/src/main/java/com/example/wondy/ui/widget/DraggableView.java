package com.example.wondy.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.example.wondy.R;
import com.example.wondy.util.L;


/**
 * @author Jose m lechon on 09/02/16.
 * @version 0.1.0
 * @since 1
 * todo read http://cyrilmottier.com/2014/05/20/custom-animations-with-fragments/
 */
public class DraggableView extends LinearLayout implements View.OnTouchListener {

    static final String TAG = DraggableView.class.getSimpleName();

    private static final int NUM_CHILD_VALID = 3;


    private static final int PERCENTAGE_LIMIT_SCREEN_TO_ANIMATE_TO_CENTER = 30;

    private static final int PERCENTAGE_LIMIT_SCREEN_TO_ANIMATE_TO_TOP = 75;

    private int mDraggableViewId;
    private View mDraggableView;

    private int mBodyViewId;
    private View mBodyView;

    private int mHideViewId;
    private View mHideView;


    private float mPointerOffset;


    private Boolean isHalfScreen;


    private int mRootViewHeight;


    public DraggableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray viewAttrs = context.obtainStyledAttributes(attrs, R.styleable.DraggableView);

        mBodyViewId = viewAttrs.getResourceId(R.styleable.DraggableView_body_view, 0);
        if (mBodyViewId == 0) {
            throw new IllegalArgumentException(viewAttrs.getPositionDescription() +
                    ": The required attribute body_view  must refer to a valid child view placed as the first child");
        }

        mDraggableViewId = viewAttrs.getResourceId(R.styleable.DraggableView_draggable_view, 0);
        if (mDraggableViewId == 0) {
            throw new IllegalArgumentException(viewAttrs.getPositionDescription() +
                    ": The required attribute  draggable_view must refer to a valid child view placed in the second position.");
        }

        mHideViewId = viewAttrs.getResourceId(R.styleable.DraggableView_hide_view, 0);
        if (mHideViewId == 0) {
            throw new IllegalArgumentException(viewAttrs.getPositionDescription() +
                    ": The required attribute hide_view must refer to a valid child view placed  placed in the last position ");
        }

        isHalfScreen = viewAttrs.getBoolean(R.styleable.DraggableView_half_screen, Boolean.TRUE);

        viewAttrs.recycle();

    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        validatingCorrectPlaceOfEachChildView();

        findingEssentialViews();

        mDraggableView.setOnTouchListener(this);


    }


    private void validatingCorrectPlaceOfEachChildView() {

        if (getChildCount() < NUM_CHILD_VALID)
            throw new RuntimeException("Your have to include almost 3 child views inside DraggableView ");

        if (getChildCount() > NUM_CHILD_VALID)
            throw new RuntimeException("Your can only have 3 child views inside DraggableView ");

        if (getChildAt(0).getId() != mBodyViewId)
            throw new RuntimeException("Body view has to be placed as the first ChildView ");

        if (getChildAt(1).getId() != mDraggableViewId)
            throw new RuntimeException("Draggable view has to be placed between BodyView and HidedView ");
    }

    private void findingEssentialViews() {

        mDraggableView = findViewById(mDraggableViewId);
        if (mDraggableView == null) {
            String name = getResources().getResourceEntryName(mDraggableViewId);
            throw new RuntimeException("Your need a child View whose id attribute is 'R.id." + name + "'");

        }

        mBodyView = findViewById(mBodyViewId);
        if (mBodyView == null) {
            String name = getResources().getResourceEntryName(mBodyViewId);
            throw new RuntimeException("Your need a child View whose id attribute is 'R.id." + name + "'");

        }

        mHideView = findViewById(mHideViewId);
        if (mHideView == null) {
            String name = getResources().getResourceEntryName(mHideViewId);
            throw new RuntimeException("Your need a child View whose id attribute is 'R.id." + name + "'");
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // Only capture drag events if we start
        if (view != mDraggableView) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            mPointerOffset = event.getRawY() - getBodyViewSize();

            mRootViewHeight = getMeasuredHeight();

            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            checkIfViewsArePlacedInTheRightPlace();

            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            setBodyViewHeight((int) (event.getRawY() - mPointerOffset));


        }
        return true;
    }


    private void checkIfViewsArePlacedInTheRightPlace() {


        int bottomDraggable = mDraggableView.getBottom();

        int currentPlace = ((mRootViewHeight - bottomDraggable) * 100) / mRootViewHeight;

        L.LOGD(TAG, " Root View Height:" + mRootViewHeight + " topDraggable: " + bottomDraggable + " currentPlace " + currentPlace);

        if (currentPlace > PERCENTAGE_LIMIT_SCREEN_TO_ANIMATE_TO_TOP) {
            doAnimationToTheTop();
        } else if (currentPlace > PERCENTAGE_LIMIT_SCREEN_TO_ANIMATE_TO_CENTER) {

            doAnimationToTheCentre();
        } else {

            doAnimationToTheBottom();
        }

    }

    private void doAnimationToTheTop() {

        L.LOGD(TAG, " doAnimationToTheTop ");

        int movementY = (mRootViewHeight / 6) - (mDraggableView.getTop());

        animateTo(movementY);
    }

    private void doAnimationToTheCentre() {

        L.LOGD(TAG, " doAnimationToTheCentre ");

        int movementY = (mRootViewHeight / 2) - (mDraggableView.getTop());

        animateTo(movementY);
    }


    private void doAnimationToTheBottom() {

        L.LOGD(TAG, " doAnimationToTheBottom ");

        int mBottomPlace = mRootViewHeight - mDraggableView.getBottom();
        animateTo(mBottomPlace);
    }


    private void animateTo(int movementY) {

        L.LOGD(TAG, " Movement " + movementY);

        ValueAnimator animator = ValueAnimator.ofFloat(mBodyView.getMeasuredHeight(), mBodyView.getMeasuredHeight() + movementY);

        animator.setInterpolator(new OvershootInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = ((Float) animation.getAnimatedValue()).intValue();

                L.LOGD(TAG, "value=" + value);
                setBodyViewHeight(value);
                mDraggableView.setBottom(value);
            }
        });

        animator.start();
    }


    private boolean setBodyViewHeight(int newHeight) {

        newHeight = Math.max(0, newHeight);

        newHeight = Math.min(newHeight, getMeasuredHeight() - mDraggableView.getMeasuredHeight());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBodyView.getLayoutParams();
        if (mHideView.getMeasuredHeight() < 1 && newHeight > params.height) {
            return false;
        }


        if (newHeight >= 0) {
            params.height = newHeight;
            params.weight = 0;
        }
        unMinimizeSecondaryContent();
        mBodyView.setLayoutParams(params);
        return true;
    }

    private void unMinimizeSecondaryContent() {
        LinearLayout.LayoutParams secondaryParams = (LinearLayout.LayoutParams) mHideView
                .getLayoutParams();
        // set the secondary content parameter to use all the available space
        secondaryParams.weight = 1;
        mHideView.setLayoutParams(secondaryParams);

    }


    public View getDraggableView() {
        return mDraggableView;
    }


    public int getBodyViewSize() {
        return mBodyView.getMeasuredHeight();
    }

}
