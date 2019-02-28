package dev.colibri.githubclienttest.fragment;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.activity.MainActivity;
import dev.colibri.githubclienttest.adapter.RepositoryAdapter;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

public class RepoListFragment extends Fragment {
    public static final String TAG = "RepoListFragment";

    private HttpClient httpClient;
    private RepositoryAdapter repositoryAdapter;
    private ProgressBar progressBar;

    private MainActivity mainActivity;

    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity) {
            mainActivity = ((MainActivity) context);
        }
        else {
            throw new RuntimeException("Can't cast context to MainActivity!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_list, container, false);

        initRecyclerView(view);

        progressBar = view.findViewById(R.id.progress_bar);
        httpClient = new HttpClient();
        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.repository_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RepositoryAdapter.OnRepoClickListener listener = (repository) -> {
            mainActivity.navigateToDetailsScreen(repository.getName(), repository.getOwner().getLogin());
        };
        repositoryAdapter = new RepositoryAdapter(listener);
        recyclerView.setAdapter(repositoryAdapter);
    }

    public void onSearchQueryChanged(String query) {
        if(query.isEmpty()) {
            Toast.makeText(getActivity(), R.string.empty_text, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}