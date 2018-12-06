package dev.colibri.githubclienttest.activity;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

public class RepoDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_REPO_NAME = "repoName";
    public static final String EXTRA_USER_LOGIN = "userLogin";
    private static final String LOG_TAG = "dc.RepoDetailsActivity";
    private HttpClient httpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        String repoName = getIntent().getStringExtra(EXTRA_REPO_NAME);
        String userLogin = getIntent().getStringExtra(EXTRA_USER_LOGIN);

        httpClient = new HttpClient();

        new GetRepositoryAsyncTask().execute(repoName, userLogin);
    }

    private class GetRepositoryAsyncTask extends AsyncTask<String, Void, Repository> {

        @Override
        protected Repository doInBackground(String... params) {
            String repoName = params[0];
            String userLogin = params[1];
            try {
                return httpClient.getRepository(repoName, userLogin);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Repository repository) {
            if(repository != null) {
                Log.d(LOG_TAG, repository.toString());
            } else {
                Toast.makeText(RepoDetailsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}