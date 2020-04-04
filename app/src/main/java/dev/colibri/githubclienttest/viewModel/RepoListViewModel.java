package dev.colibri.githubclienttest.viewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import dev.colibri.githubclienttest.app.App;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoListViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQueryValidationException = new MutableLiveData<>();
    private MutableLiveData<List<Repository>> repositories = new MutableLiveData<>();

    private DataRepository dataRepository = App.getDataRepository();

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<List<Repository>> getRepositories() {
        return repositories;
    }

    public MutableLiveData<Boolean> getIsNetworkException() {
        return isNetworkException;
    }

    public MutableLiveData<Boolean> getIsQueryValidationException() {
        return isQueryValidationException;
    }

    public void loadRepositories(String query) {
        if (query == null) {
            isQueryValidationException.setValue(true);
        } else {
            new GetRepositoriesAsyncTask().execute(query);
        }
    }

    public class GetRepositoriesAsyncTask extends AsyncTask<String, Void, List<Repository>> {

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

            if (result != null) {
                repositories.setValue(result);
            } else {
                isNetworkException.setValue(true);
            }
        }
    }
}
