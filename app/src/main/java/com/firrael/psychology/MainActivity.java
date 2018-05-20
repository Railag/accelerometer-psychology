package com.firrael.psychology;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.firrael.psychology.model.User;
import com.firrael.psychology.presenter.MainPresenter;
import com.firrael.psychology.view.InfoFragment;
import com.firrael.psychology.view.InstructionFragment;
import com.firrael.psychology.view.MenuFragment;
import com.firrael.psychology.view.SettingsFragment;
import com.firrael.psychology.view.SplashFragment;
import com.firrael.psychology.view.StartFragment;
import com.firrael.psychology.view.StatisticsFragment;
import com.firrael.psychology.view.TestsFragment;
import com.firrael.psychology.view.register.AgeFragment;
import com.firrael.psychology.view.register.LoginFragment;
import com.firrael.psychology.view.register.RegisterFragment;
import com.firrael.psychology.view.register.TimeFragment;
import com.firrael.psychology.view.results.AttentionStabilityResultsFragment;
import com.firrael.psychology.view.results.EnglishResultsFragment;
import com.firrael.psychology.view.results.FocusingResultsFragment;
import com.firrael.psychology.view.results.ResultScreen;
import com.firrael.psychology.view.results.StressResistanceResultsFragment;
import com.firrael.psychology.view.tests.AccelerometerTestFragment;
import com.firrael.psychology.view.tests.AttentionStabilityTestFragment;
import com.firrael.psychology.view.tests.EnglishTestFragment;
import com.firrael.psychology.view.tests.FocusingTestFragment;
import com.firrael.psychology.view.tests.StressResistanceTestFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wang.avi.AVLoadingIndicatorView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter> {

    private static final String TAG_MAIN = "mainTag";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.toolbarExit)
    TextView toolbarExit;

    private FirebaseAnalytics analytics;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        App.setMainActivity(this);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        toolbarExit.setOnClickListener(view -> {
            toggleExit(false);
            User.logout(this);
            toSplash();
        });

        analytics = FirebaseAnalytics.getInstance(this);

        hideToolbar();

        toSplash();

        checkForUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }


    @Override
    public void onBackPressed() {

        if (currentFragment instanceof ResultScreen) {
            toMenu();
            return;
        }

        if (currentFragment instanceof LoginFragment || currentFragment instanceof RegisterFragment) {
            hideToolbar();
            toStart();
            return;
        }

        if (currentFragment instanceof MenuFragment || currentFragment instanceof StartFragment) {
            finish();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        loading.setVisibility(View.GONE);
    }

    private <T extends Fragment> void setFragment(final T fragment) {
        runOnUiThread(() -> {
            currentFragment = fragment;

            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            // TODO custom transaction animations
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            fragmentTransaction.replace(R.id.mainFragment, fragment, TAG_MAIN);
            fragmentTransaction.commitAllowingStateLoss();

        });
    }

    public void setCurrentFragment(Fragment fragment) {
        this.currentFragment = fragment;
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    public void blueTop() {
        showToolbar();

        setStatusBarColor(R.color.toolbarColor);
    }

    public void setStatusBarColor(int color) {
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        window.setStatusBarColor(getResources().getColor(color));
    }

    public void transparentStatusBar() {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void toggleArrow(boolean visible) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(visible);
        }
    }

    public void toggleExit(boolean visible) {
        toolbarExit.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void toSplash() {
        setFragment(SplashFragment.newInstance());
    }

    public void toStart() {
        setFragment(StartFragment.newInstance());
    }

    public void toLogin() {
        setFragment(LoginFragment.newInstance());
    }

    public void toTests() {
        setFragment(TestsFragment.newInstance());
    }

    public void toNameScreen() {
        setFragment(RegisterFragment.newInstance());
    }

    public void toAgeScreen() {
        setFragment(AgeFragment.newInstance());
    }

    public void toTimeScreen() {
        setFragment(TimeFragment.newInstance());
    }

    public void toMenu() {
        setFragment(MenuFragment.newInstance());
    }

    public void toInfo() {
        setFragment(InfoFragment.newInstance());
    }

    public void toStatistics() {
        setFragment(StatisticsFragment.newInstance());
    }


    public void toSettings() {
        setFragment(SettingsFragment.newInstance());
    }

    public void toInstructionFragment(InstructionFragment.Test test) {
        setFragment(InstructionFragment.newInstance(test));
    }

    public void toFocusingTest() {
        setFragment(FocusingTestFragment.newInstance());
    }

    public void toAttentionStabilityTest() {
        setFragment(AttentionStabilityTestFragment.newInstance());
    }

    public void toStressResistanceTest() {
        setFragment(StressResistanceTestFragment.newInstance());
    }

    public void toEnglishTest() {
    //    setFragment(EnglishTestFragment.newInstance());
        toAccelerometerTest();
    }

    public void toAccelerometerTest() {
        setFragment(AccelerometerTestFragment.newInstance());
    }

    public void toFocusingResults(Bundle args) {
        setFragment(FocusingResultsFragment.newInstance(args));
    }

    public void toAttentionStabilityResults(Bundle args) {
        setFragment(AttentionStabilityResultsFragment.newInstance(args));
    }

    public void toStressResistanceResults(Bundle args) {
        setFragment(StressResistanceResultsFragment.newInstance(args));
    }

    public void toEnglishResults(Bundle args) {
        setFragment(EnglishResultsFragment.newInstance(args));
    }
}