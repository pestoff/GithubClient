package dev.colibri.githubclienttest.activity;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.adapter.RepositoryAdapter;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "dc.MainActivity";

    private Toolbar toolbar;

    private HttpClient httpClient;
    private RepositoryAdapter repositoryAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();

        progressBar = findViewById(R.id.progress_bar);

        httpClient = new HttpClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        repositoryAdapter.clearItems();
                        new GetRepositoriesAsyncTask().execute(s);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                }
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.settings_toolbar) : {
                Toast.makeText(this, R.string.settings_clicked, Toast.LENGTH_SHORT).show();
            }
        }
        return true;
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


    private class GetRepositoriesAsyncTask extends AsyncTask<String, Void, List<Repository>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Repository> doInBackground(String... queries) {

            try {
                return httpClient.getRepositories(queries[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Repository> result) {
            progressBar.setVisibility(View.GONE);

            if(result != null) {
                repositoryAdapter.addItems(result);
            } else {
                Toast.makeText(MainActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}