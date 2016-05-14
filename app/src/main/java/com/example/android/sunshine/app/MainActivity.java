package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.sunshine.app.index.ForecastsFragment;
import com.example.android.sunshine.app.shell.ShellContract;
import com.example.android.sunshine.app.shell.ShellContract.Feature;


public class MainActivity extends AppCompatActivity implements ShellContract.Navigation {
    private FragmentManager fragMan;
    private Sunshine.ActivityInjector injector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragMan = getSupportFragmentManager();
        if (savedInstanceState == null) {
            home();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        initInjector();
        if (fragment instanceof ForecastsFragment) {
            injector.index().inject((ForecastsFragment) fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void launch(@Feature int feature) {
        switch (feature) {
            case Feature.DETAIL:
                break;
            case Feature.INDEX:
                if (fragMan.getBackStackEntryCount() == 0) {
                    fragMan.beginTransaction()
                            .add(R.id.container, new ForecastsFragment(), "index")
                            .commit();
                }
                break;
        }
    }

    @Override
    public void back() {
        fragMan.popBackStack();
    }

    @Override
    public void home() {
        launch(Feature.INDEX);
    }

    @Override
    public void quit() {
        finish();
    }

    private void initInjector() {
        // it has to be like this because onAttachFragment is sometimes called _before_
        // onCreate! during config change to be specific. but i can't always call inject(this)
        // in onAttachFragment because it is called every time a fragment is added.
        if (injector == null) {
            injector = ((Sunshine) getApplication()).getInjector(this);
            injector.inject(this);
        }
    }
}
