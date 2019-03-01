package dev.colibri.githubclienttest.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoListViewModel extends ViewModel {
    private DataRepository dataRepository;

    private MutableLiveData<List<Repository>> repositories = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQueryValidationException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @Inject
    public RepoListViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public LiveData<List<Repository>> geRepositories() {
        return repositories;
    }


    public LiveData<Boolean> isNetworkException() {
        return isNetworkException;
    }

    public LiveData<Boolean> isQueryValidationException() {
        return isQueryValidationException;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void searchRepositories(String query) {
        if(query.isEmpty()) {
            isQueryValidationException.setValue(true);
        } else {
            new GetRepositoriesAsyncTask().execute(query);
        }
    }

    private class GetRepositoriesAsyncTask extends AsyncTask<String, Void, List<Repository>> {

        @Override
        protected void onPreExecute() {
            isLoading.setValue(true);
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
            isLoading.setValue(false);

            if(result != null) {
                repositories.setValue(result);
            } else {
                isNetworkException.setValue(true);
            }
        }
    }

}
