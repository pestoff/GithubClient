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

    private MutableLiveData<List<Repository>> repositories;
    private MutableLiveData<Boolean> isNetworkException;
    private MutableLiveData<Boolean> isQueryValidationException;
    private MutableLiveData<Boolean> isLoading;

    @Inject
    public RepoListViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public LiveData<List<Repository>> geRepositories() {
        if (repositories == null) {
            repositories = new MutableLiveData<>();
        }
        return repositories;
    }


    public LiveData<Boolean> isNetworkException() {
        if(isNetworkException == null) {
            isNetworkException = new MutableLiveData<>();
        }
        return isNetworkException;
    }

    public LiveData<Boolean> isQueryValidationException() {
        if(isQueryValidationException == null) {
            isQueryValidationException = new MutableLiveData<>();
        }
        return isQueryValidationException;
    }

    public LiveData<Boolean> isLoading() {
        if(isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public void searchRepositories(String query) {
        if(query.isEmpty()) {
            // don't want value to be saved after screen rotation
            isQueryValidationException.setValue(true);
            isQueryValidationException.setValue(false);
        } else {
            new GetRepositoriesAsyncTask().execute(query);
        }
    }

    private class GetRepositoriesAsyncTask extends AsyncTask<String, Void, ArrayList<Repository>> {

        @Override
        protected void onPreExecute() {
            isLoading.setValue(true);
        }

        @Override
        protected ArrayList<Repository> doInBackground(String... queries) {

            try {
                return dataRepository.getRepositories(queries[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Repository> result) {
            isLoading.setValue(false);

            if(result != null) {
                repositories.setValue(result);
            } else {
                // don't want value to be saved after screen rotation
                isNetworkException.setValue(true);
                isNetworkException.setValue(false);
            }
        }
    }

}
