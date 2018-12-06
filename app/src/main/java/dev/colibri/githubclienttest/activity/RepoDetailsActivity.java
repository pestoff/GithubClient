package dev.colibri.githubclienttest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import dev.colibri.githubclienttest.R;

public class RepoDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_REPO_NAME = "repoName";
    public static final String EXTRA_USER_LOGIN = "userLogin";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        String repoName = getIntent().getStringExtra(EXTRA_REPO_NAME);
        String userLogin = getIntent().getStringExtra(EXTRA_USER_LOGIN);

        Toast.makeText(this, repoName + " " + userLogin, Toast.LENGTH_SHORT).show();
    }
}