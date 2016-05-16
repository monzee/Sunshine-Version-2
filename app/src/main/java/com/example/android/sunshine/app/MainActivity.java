package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.sunshine.app.index.ForecastsFragment;
import com.example.android.sunshine.app.shell.ShellContract;
import com.example.android.sunshine.app.shell.ShellWiring;

import java.util.ArrayDeque;
import java.util.Queue;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {
    private Sunshine.ActivityInjector injector;
    private final Queue<Fragment> fragments = new ArrayDeque<>();

    @Inject
    ShellContract.Navigation go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        injector = ((Sunshine) getApplication()).getInjector(this);
        ShellWiring wiring = new ShellWiring(
                getSupportFragmentManager(),
                R.id.container
        );
        injector.shell(wiring).inject(this);
        if (savedInstanceState == null) {
            go.home();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        fragments.add(fragment);
    }

    @Override
    protected void onResumeFragments() {
        while (!fragments.isEmpty()) {
            inject(fragments.remove());
        }
        super.onResumeFragments();
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

    private void inject(Fragment fragment) {
        if (fragment instanceof ForecastsFragment) {
            injector.index().inject((ForecastsFragment) fragment);
        }
    }
}
