package dev.colibri.githubclienttest.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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

import javax.inject.Inject;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.adapter.RepositoryAdapter;
import dev.colibri.githubclienttest.app.App;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;
import dev.colibri.githubclienttest.repository.DataRepository;
import dev.colibri.githubclienttest.viewModel.RepoListViewModel;
import dev.colibri.githubclienttest.viewModelFactory.ViewModelFactory;

public class RepoListFragment extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private RepoListViewModel repoListViewModel;

    private repositorySelectedCallback callback;

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

        App.getAppComponent().inject(this);

        initRecyclerView(view);
        initViewModel();

        progressBar = view.findViewById(R.id.progress_bar);

        return view;
    }

    private void initViewModel() {
        repoListViewModel = ViewModelProviders.of(this, viewModelFactory).get(RepoListViewModel.class);

        repoListViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        repoListViewModel.getRepositories().observe(this, repositories -> {
            repositoryAdapter.addItems(repositories);
        });

        repoListViewModel.getIsNetworkException().observe(this, isException -> {
            if (isException) {
                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });

        repoListViewModel.getIsQueryValidationException().observe(this, isException -> {
            if (isException) {
                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });
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
        repoListViewModel.loadRepositories(request);
    }

    public interface repositorySelectedCallback {
        void onRepositorySelected(Repository repository);
    }
}
