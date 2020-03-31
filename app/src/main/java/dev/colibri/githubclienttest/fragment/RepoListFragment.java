package dev.colibri.githubclienttest.fragment;

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

import java.io.IOException;
import java.util.List;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.adapter.RepositoryAdapter;
import dev.colibri.githubclienttest.app.App;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoListFragment extends Fragment {

    private repositorySelectedCallback callback;

    private DataRepository dataRepository;
    private RepositoryAdapter repositoryAdapter;
    private ProgressBar progressBar;

    public RepoListFragment() {
    }

    public static RepoListFragment newInstance() {
        return new RepoListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_list, container, false);

        initRecyclerView(view);

        progressBar = view.findViewById(R.id.progress_bar);

        dataRepository = App.getDataRepository();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof repositorySelectedCallback) {
            callback = (repositorySelectedCallback) context;
        } else {
            throw new ClassCastException("Activity shall implement repositorySelectedCallback interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.repository_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RepositoryAdapter.OnRepoClickListener listener = new RepositoryAdapter.OnRepoClickListener() {
            @Override
            public void onRepoClick(Repository repository) {
                callback.onRepositorySelected(repository);
            }
        };
        repositoryAdapter = new RepositoryAdapter(listener);
        recyclerView.setAdapter(repositoryAdapter);
    }


    public void onSearchQueryChanged(String request) {
        repositoryAdapter.clearItems();
        new GetRepositoriesAsyncTask().execute(request);
    }

    public class GetRepositoriesAsyncTask extends AsyncTask<String, Void, List<Repository>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Repository> doInBackground(String... queries) {

            try {
                return dataRepository.getRepositories(queries[0]);
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
                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface repositorySelectedCallback {
        public void onRepositorySelected(Repository repository);
    }
}
