package dev.colibri.githubclienttest.fragment;

import javax.inject.Inject;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import dev.colibri.githubclienttest.App;
import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.activity.MainActivity;
import dev.colibri.githubclienttest.adapter.RepositoryAdapter;
import dev.colibri.githubclienttest.di.ViewModelFactory;
import dev.colibri.githubclienttest.viewmodel.RepoListViewModel;

public class RepoListFragment extends Fragment {
    public static final String TAG = "RepoListFragment";

    private RepositoryAdapter repositoryAdapter;
    private ProgressBar progressBar;
    private MainActivity mainActivity;

    private RepoListViewModel viewModel;
    @Inject ViewModelFactory viewModelFactory;

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
        App.getAppComponent().inject(this);

        initRecyclerView(view);
        initViewModel();

        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RepoListViewModel.class);

        viewModel.geRepositories().observe(this, (repositories -> {
            if(repositories != null) {
                repositoryAdapter.addItems(repositories);
            }
        }));

        viewModel.isLoading().observe(this, (isLoading) -> {
            if(isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.isNetworkException().observe(this, (isException) -> {
            if(isException != null && isException) {
                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.isQueryValidationException().observe(this, (isQueryValidationException) -> {
            if(isQueryValidationException != null && isQueryValidationException) {
                Toast.makeText(getActivity(), R.string.empty_text, Toast.LENGTH_SHORT).show();
            }
        });
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
        viewModel.searchRepositories(query);
    }
}