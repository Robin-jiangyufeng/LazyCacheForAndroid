package com.robin.lazy.sample;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.core.deps.guava.util.concurrent.Uninterruptibles;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

/**
 * 文 件 名:  MainActivityTest.java
 * 版    权:  Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  江钰锋 00501
 * 修改时间:  16/6/6
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testOnCreate() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.buttonSave)).perform(ViewActions.click());
        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
        Espresso.onView(ViewMatchers.withId(R.id.buttonLoad)).perform(ViewActions.click());
    }

}