package com.hooooong.pholar.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Heepie on 2017. 11. 10..
 */

public class ScrollingToolbarBehavior extends CoordinatorLayout.Behavior<Toolbar> {

    public ScrollingToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Toolbar child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Toolbar child, View dependency) {
        if (dependency instanceof AppBarLayout) {

            int distanceToScroll = child.getHeight();

            int bottomToolbarHeight = child.getHeight();

            float ratio = dependency.getY() / (float) bottomToolbarHeight;
            child.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}