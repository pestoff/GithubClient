package dev.colibri.githubclienttest.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.adapter.RepositoryAdapter;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "dc.MainActivity";

    private HttpClient httpClient;
    private RepositoryAdapter repositoryAdapter;
    private EditText queryEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        queryEditText = findViewById(R.id.query_edit_text);
        progressBar = findViewById(R.id.progress_bar);

        httpClient = new HttpClient(new OkHttpClient());
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.repository_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RepositoryAdapter.OnRepoClickListener listener = new RepositoryAdapter.OnRepoClickListener() {
            @Override
            public void onRepoClick(Repository repository) {
                Intent intent = new Intent(MainActivity.this, RepoDetailsActivity.class);
                intent.putExtra(RepoDetailsActivity.EXTRA_REPO_NAME, repository.getName());
                String userLogin = repository.getOwner().getLogin();
                intent.putExtra(RepoDetailsActivity.EXTRA_USER_LOGIN, userLogin);
                startActivity(intent);
            }
        };
        repositoryAdapter = new RepositoryAdapter(listener);
        recyclerView.setAdapter(repositoryAdapter);
    }

    public void searchRepositories(View v) {
        String query = queryEditText.getText().toString();
        if(query.isEmpty()) {
            Toast.makeText(this, R.string.empty_text, Toast.LENGTH_SHORT).show();
        } else {
            repositoryAdapter.clearItems();
            new GetRepositoriesAsyncTask().execute(query);
        }
    }

    private class GetRepositoriesAsyncTask extends AsyncTask<String, Void, ArrayList<Repository>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Repository> doInBackground(String... queries) {

            try {
                return httpClient.getRepositories(queries[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Repository> result) {
            progressBar.setVisibility(View.GONE);

            if(result != null) {
                repositoryAdapter.addItems(result);
            } else {
                Toast.makeText(MainActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}