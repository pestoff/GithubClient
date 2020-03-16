package dev.colibri.githubclienttest.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.fragment.RepoDetailsFragment;
import dev.colibri.githubclienttest.fragment.RepoListFragment;

public class MainActivity extends AppCompatActivity
    implements RepoListFragment.repositorySelectedCallback {

    private Toolbar toolbar;

    private RepoListFragment listFragment;
    private RepoDetailsFragment detailsFragment;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listFragment = RepoListFragment.newInstance();

        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .commit();
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
                        listFragment.onSearchQueryChanged(s);
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

    @Override
    public void onRepositorySelected(Repository repository) {
        if (detailsFragment == null) {
            detailsFragment = RepoDetailsFragment.newInstance(repository);
        } else {
            detailsFragment.updateContent(repository);
        }

        if (findViewById(R.id.detail_fragment_container) == null) {
            manager.beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            manager.beginTransaction()
                    .replace(R.id.detail_fragment_container, detailsFragment)
                    .commit();
        }
    }
}