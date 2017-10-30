package com.example.thatnight.wanandroid.view;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by thatnight on 2017.10.30.
 */

public class FloatingBehavior extends CoordinatorLayout.Behavior<View> {

    private boolean isAnimate;


    public FloatingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull View child,
                                  @NonNull View target,
                                  int dx,
                                  int dy,
                                  @NonNull int[] consumed,
                                  int type) {
        if (dy >= 0 && !isAnimate && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (dy < 0 && !isAnimate && child.getVisibility() == View.INVISIBLE) {
            show(child);
        }
    }

    private void show(final View child) {
        ViewPropertyAnimator animator = child.animate().translationY(0).setInterpolator(new FastOutSlowInInterpolator()).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                child.setVisibility(View.VISIBLE);
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                hide(child);
                isAnimate = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void hide(final View child) {
        ViewPropertyAnimator animator = child.animate().translationY(child.getHeight()).setInterpolator(new FastOutSlowInInterpolator()).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                child.setVisibility(View.INVISIBLE);
                isAnimate = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                show(child);
                isAnimate = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


}
