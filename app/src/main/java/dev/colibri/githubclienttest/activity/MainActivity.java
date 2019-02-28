package dev.colibri.githubclienttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.fragment.RepoDetailsFragment;
import dev.colibri.githubclienttest.fragment.RepoListFragment;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "dc.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.master_fragment_container, new RepoListFragment(), RepoListFragment.TAG)
                    .commit();
        }

        initView();
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView searchImageView = findViewById(R.id.search_button);
        final EditText searchEditText = findViewById(R.id.query_edit_text);

        searchImageView.setOnClickListener(v -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.master_fragment_container);
            final RepoListFragment repoListFragment = (RepoListFragment) fragment;
            if(repoListFragment == null) throw new NullPointerException("Fragment can't be null");

            String query = searchEditText.getText().toString();
            repoListFragment.onSearchQueryChanged(query);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, R.string.settings_clicked_hint, Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void navigateToDetailsScreen(String name, String login) {
        boolean isDualPaneMode = findViewById(R.id.details_fragment_container) != null;

        if(isDualPaneMode) {
            showDetailsFragment(name, login);
        } else {
            startDetailsActivity(name, login);
        }
    }

    private void showDetailsFragment(String name, String login) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.details_fragment_container);
        if(fragment != null) {
            RepoDetailsFragment repoDetailsFragment = (RepoDetailsFragment) fragment;
            repoDetailsFragment.updateContent(name, login);
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.details_fragment_container, RepoDetailsFragment.newInstance(name, login))
                    .commit();
        }
    }

    private void startDetailsActivity(String name, String login) {
        Intent intent = new Intent(this, RepoDetailsActivity.class);
        intent.putExtra(RepoDetailsActivity.EXTRA_REPO_NAME, name);
        intent.putExtra(RepoDetailsActivity.EXTRA_USER_LOGIN, login);
        startActivity(intent);
    }

}