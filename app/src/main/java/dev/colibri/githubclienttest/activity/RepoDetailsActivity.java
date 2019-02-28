package dev.colibri.githubclienttest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.fragment.RepoDetailsFragment;

public class RepoDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_REPO_NAME = "repoName";
    public static final String EXTRA_USER_LOGIN = "userLogin";
    private static final String LOG_TAG = "dc.RepoDetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        String repoName = getIntent().getStringExtra(EXTRA_REPO_NAME);
        String userLogin = getIntent().getStringExtra(EXTRA_USER_LOGIN);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.repo_details_fragment);
        if(fragment == null) throw new NullPointerException("Fragment can't be null");

        RepoDetailsFragment repoDetailsFragment = (RepoDetailsFragment) fragment;
        repoDetailsFragment.updateContent(repoName, userLogin);

    }
}
