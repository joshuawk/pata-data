package com.jlt.patadata;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import static android.content.SharedPreferences.*;

/**
 *
 * Pata Data - Displays World Bank JSON on Kenya
 *
 * Copyright (C) 2016 Kairu Joshua Wambugu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */

// begin activity MainActivity
// is the main activity orchestrating the fragments
public class MainActivity extends AppCompatActivity implements RequestURLListener, SelectedDatasetListener, ResponseJSONListener, LineChartAnimationListener , PreferencesInterface {

    /** CONSTANTS */

    public static final String

    /** Fragment Constants */

    FRAGMENT_CHOOSE_DATASET = "FRAGMENT_CHOOSE_DATASET", // name to identify the choosing dataset fragment in the back stack

    FRAGMENT_TABLE_DISPLAY_DATASET = "FRAGMENT_TABLE_DISPLAY_DATASET", // name to identify the table display dataset fragment in the back stack

    FRAGMENT_CHART_DISPLAY_DATASET = "FRAGMENT_CHART_DISPLAY_DATASET", // name to identify the chart display dataset fragment in the back stack

    /** Preference Constants */

    PREFERENCES = "PREFERENCES", // string to identify the preferences

    PREFERENCE_SELECTED_DATASET_NAME = "PREFERENCE_SELECTED_DATASET_NAME", // string to identify the preference for storing the selected dataset's name

    PREFERENCE_SELECTED_DATASET_START_YEAR = "PREFERENCE_SELECTED_DATASET_START_YEAR", // string to identify the preference for storing the selected dataset's start year

    PREFERENCE_SELECTED_DATASET_END_YEAR = "PREFERENCE_SELECTED_DATASET_END_YEAR", // string to identify the preference for storing the selected dataset's end year

    PREFERENCE_REQUEST_URL = "PREFERENCE_REQUEST_URL", // string to identify the preference for storing the request URL

    PREFERENCE_RESPONSE_JSON = "PREFERENCE_RESPONSE_JSON", // string to identify the preference for storing the response JSON

    PREFERENCE_LINE_CHART_ANIMATION_FREQUENCY = "PREFERENCE_LINE_CHART_ANIMATION_FREQUENCY", // string to identify the preference for storing the how many times the line chart should be animated

    PREFERENCE_LINE_CHART_ANIMATE_ONCE = "PREFERENCE_LINE_CHART_ANIMATE_ONCE", // string to specify the line chart should be animated only once

    PREFERENCE_LINE_CHART_ANIMATE_ALWAYS = "PREFERENCE_LINE_CHART_ANIMATE_ALWAYS", // string to specify the line chart should be animated always

    PREFERENCE_LINE_CHART_ANIMATED_ONCE = "PREFERENCE_LINE_CHART_ANIMATED_ONCE"; // string to specify whether the line chart has been animated once

    /** VARIABLES */

    /** Fragments */

    private Fragment currentFragment; // the fragment currently on the screen

    /** METHODS */
//
//    /** Strings */
//
//    private String
//
//    requestURL, // the request URL
//
//    selectedDatasetName; // the name of the selected dataset
//

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin onCreate
    public void onCreate( Bundle savedInstanceState ) {

        // 0. super things
        // 0a. make sure the chart is animated once
        // 1. use the main activity layout
        // 1a. set the bar title to be the name of the app
        // 2. if the app is running first time (so the saved instance state will be null) (done to avoid blushes with screen rotation)
        // 2a. start off the choose dataset fragment
        // 2a1. add the choose dataset fragment to the backstack
        // 3. if the app is not running first time (so the saved instance state will not be null)
        // 3a. set the app bar title to be what was stored in the saved instance

        // 0. super things

        super.onCreate( savedInstanceState );

        // 0a. make sure the chart is animated once

        // begin if for if the line chart preference is not to animate once
        // this can be if the preference has not been set yet or has been set a a different value
        if (
                getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getString( PREFERENCE_LINE_CHART_ANIMATION_FREQUENCY, null ) == null
                ||
                getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getString( PREFERENCE_LINE_CHART_ANIMATION_FREQUENCY, null ).equals( PREFERENCE_LINE_CHART_ANIMATE_ONCE ) == false ) {

            Editor editor = getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).edit();

            editor.putString( PREFERENCE_LINE_CHART_ANIMATION_FREQUENCY, PREFERENCE_LINE_CHART_ANIMATE_ONCE );

            editor.apply();

        } // end if for if the line chart preference is not to animate once

        // 1. use the main activity layout

        setContentView( R.layout.activity_main );

        // 2. if the app is running first time (so the saved instance state will be null) (done to avoid blushes with screen rotation)

        FragmentManager fragmentManager = getSupportFragmentManager();

        // begin if for if the choosing dataset fragment is null
        if( savedInstanceState == null ) {

            ChooseDatasetFragment chooseDatasetFragment = new ChooseDatasetFragment();

            fragmentManager

                    .beginTransaction()

                    .setCustomAnimations(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left
                    )

                    // 2a. start off the choose dataset fragment

                    .add( R.id.m_fl_content, chooseDatasetFragment )

                    // 2a1. add the choose dataset fragment to the backstack

                    .addToBackStack( FRAGMENT_CHOOSE_DATASET )

                    .commit();

        } // end if for if the choosing dataset fragment is null

        // 3. if the app is not running first time (so the saved instance state will not be null)

        // else for when the saved instance state is not null

        // 3a. set the app bar title to be what was stored in the saved instance

//        else {
//            CharSequence charSequence = savedInstanceState.getString( BUNDLE_CURRENT_APP_BAR_TITLE );
//            setTitle( charSequence ); }

    } // end onCreate

    @Override
    // begin onCreateOptionsMenu
    public boolean onCreateOptionsMenu( Menu menu ) {

        // 0. use the empty options menu
        // 1. return super things

        // 0. use the empty options menu

        getMenuInflater().inflate( R.menu.menu_activity_main, menu );

        // 1. return super things

        return super.onCreateOptionsMenu( menu );

    } // end onCreateOptionsMenu

    @Override
    // begin onBackPressed
    public void onBackPressed() {

        // 0. if the current fragment is the choose dataset fragment
        // 0a. finish
        // 1. if the current fragment is the chart display dataset fragment (or the error fragment?)
        // 1a. go back to the choose dataset fragment
        // 2. else if the current fragment is the table display dataset fragment
        // 2a. go back to the chart display dataset fragment

        FragmentManager fragmentManager = getSupportFragmentManager();

        // 0. if the current fragment is the choose dataset fragment

        // if for if the current fragment is the choose dataset fragment
        if( getCurrentFragmentName( fragmentManager ).equals( FRAGMENT_CHOOSE_DATASET ) == true )

        // 0a. finish

        { finish(); }

        // 1. if the current fragment is the display dataset fragment (or the error fragment?)

        // begin else if for when the current fragment is the chart display dataset fragment
        else if ( getCurrentFragmentName( fragmentManager ).equals( FRAGMENT_CHART_DISPLAY_DATASET ) == true ) {

            // 1a. go back to the choose dataset fragment

            fragmentManager

                    .beginTransaction()

                    .setCustomAnimations(
                        R.anim.slide_in_from_right, R.anim.slide_out_to_left
                    )

                    .replace( R.id.m_fl_content, new ChooseDatasetFragment() )

                    .addToBackStack( MainActivity.FRAGMENT_CHOOSE_DATASET )

                    .commit();

        } // end else if for when the current fragment is the display dataset fragment

        // 2. else if the current fragment is the table display dataset fragment

        // begin else if for when the current fragment is the table display dataset fragment
        else if ( getCurrentFragmentName( fragmentManager ).equals( FRAGMENT_TABLE_DISPLAY_DATASET ) == true ) {

            // 2a. go back to the chart display dataset fragment

            fragmentManager

                    .beginTransaction()

                    .setCustomAnimations( R.anim.slide_in_from_left, R.anim.slide_out_to_right )

                    .replace( R.id.m_fl_content, new ChartDisplayDatasetFragment() )

                    .addToBackStack( MainActivity.FRAGMENT_CHART_DISPLAY_DATASET )

                    .commit();

        } // end else if for when the current fragment is the table display dataset fragment

    } // end onBackPressed

    @Override
    // begin onSaveInstanceState
    // used to store the instance state for the cases of configuration changes
    public void onSaveInstanceState( Bundle outState ) {

        // 0. super things
        // 1. store app bar title

        // 0. super things

        super.onSaveInstanceState( outState );

        // 1. store app bar title

//        outState.putCharSequence( BUNDLE_CURRENT_APP_BAR_TITLE, getSupportActionBar().getTitle().toString() );


    } // end onSaveInstanceState

    @Override
    // begin onDestroy
    protected void onDestroy() {

        // 0. super things
        // 1. make sure the chart is not animated (for cases like when the user exits the app in chart display mode)

        // 0. super things

        super.onDestroy();

        // 1. make sure the chart is not animated (for cases like when the user exits the app in chart display mode)

        onSetChartAnimated( false );

    } // end onDestroy

    @Override
    // begin onSetRequestURL
    // sets the request URL to the preferences
    public void onSetRequestURL( String requestURL ) {

        // 0. set the request URL in shared preferences

        // 0. set the request URL in shared preferences

        Editor editor = getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).edit();

        editor.putString( PREFERENCE_REQUEST_URL, requestURL );

        editor.apply();

    } // end onSetRequestURL

    @Override
    // begin getRequestURL
    // gets the request URL from the preferences
    public String getRequestURL() {

        // 0. get the request URL from the shared preferences

        // 0. get the request URL from the shared preferences

        return getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getString( PREFERENCE_REQUEST_URL, null );

    } // end getRequestURL

    @Override
    // getSelectedDatasetName from preferences
    public String getSelectedDatasetName() { return getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getString( PREFERENCE_SELECTED_DATASET_NAME, null ); }

    @Override
    // begin onSetSelectedDatasetName
    // sets dataset name at preferences
    public void onSetSelectedDatasetName( String datasetName ) {

        Editor editor = getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).edit();

        editor.putString( PREFERENCE_SELECTED_DATASET_NAME, datasetName );

        editor.apply();

    } // end onSetSelectedDatasetName

    @Override
    // getSelectedDatasetStartYear from preferences
    public int getSelectedDatasetStartYear() { return getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getInt( PREFERENCE_SELECTED_DATASET_START_YEAR, -1 ); }

    @Override
    // begin onSetSelectedDatasetStartYear
    public void onSetSelectedDatasetStartYear( int datasetStartYear ) {

        Editor editor = getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).edit();

        editor.putInt( PREFERENCE_SELECTED_DATASET_START_YEAR, datasetStartYear );

        editor.apply();

    } // end onSetSelectedDatasetStartYear

    @Override
    // getSelectedDatasetEndYear
    public int getSelectedDatasetEndYear() { return getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getInt( PREFERENCE_SELECTED_DATASET_END_YEAR, -1 ); }

    @Override
    // begin onSetSelectedDatasetEndYear
    public void onSetSelectedDatasetEndYear( int datasetEndYear ) {

        Editor editor = getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).edit();

        editor.putInt( PREFERENCE_SELECTED_DATASET_END_YEAR, datasetEndYear );

        editor.apply();

    } // end onSetSelectedDatasetEndYear

    @Override
    // begin onSetResponseJSON
    // onSetResponseJSON in preferences
    public void onSetResponseJSON( String responseJSON ) {

        Editor editor = getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).edit();

        editor.putString( PREFERENCE_RESPONSE_JSON, responseJSON );

        editor.apply();

    } // end onSetResponseJSON

    @Override
    // getResponseJSON
    public String getResponseJSON() { return getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getString( PREFERENCE_RESPONSE_JSON, null ); }

    @Override
    // isChartAnimatedOnce
    public boolean isChartAnimatedOnce() { return getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getBoolean( PREFERENCE_LINE_CHART_ANIMATED_ONCE, false ); }

    @Override
    // begin onSetChartAnimated
    // onSetChartAnimated in preferences
    public void onSetChartAnimated( boolean chartAnimated ) {

        Editor editor = getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).edit();

        editor.putBoolean( PREFERENCE_LINE_CHART_ANIMATED_ONCE, chartAnimated );

        editor.apply();

    } // end onSetChartAnimated

    @Override
    // getLineChartAnimationFrequencyPreference
    public String getLineChartAnimationFrequencyPreference() { return getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE ).getString( PREFERENCE_LINE_CHART_ANIMATION_FREQUENCY, null ); }

    /**
     * Other Methods
     */

    // begin method getCurrentFragmentName
    // gets the name of the fragment currently on top of the back stack
    // using the passed fragment manager
    private String getCurrentFragmentName( FragmentManager fragmentManager ) { return fragmentManager.getBackStackEntryAt( fragmentManager.getBackStackEntryCount() - 1 ).getName(); }

} // end activity MainActivity