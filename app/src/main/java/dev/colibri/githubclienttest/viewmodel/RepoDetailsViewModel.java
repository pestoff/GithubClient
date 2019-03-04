package dev.colibri.githubclienttest.viewmodel;

import java.io.IOException;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoDetailsViewModel extends ViewModel {

    private DataRepository dataRepository;

    private MutableLiveData<Repository> repository = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @Inject
    public RepoDetailsViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public LiveData<Repository> getRepository() {
        return repository;
    }


    public LiveData<Boolean> isException() {
        return isNetworkException;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void updateContent(String repoName, String userLogin) {
        new GetRepositoryAsyncTask().execute(repoName, userLogin);
    }

    private class GetRepositoryAsyncTask extends AsyncTask<String, Void, Repository> {

        @Override
        protected void onPreExecute() {
            isLoading.setValue(true);
        }

        @Override
        protected Repository doInBackground(String... params) {
            String repoName = params[0];
            String userLogin = params[1];
            try {
                return dataRepository.getRepository(repoName, userLogin);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Repository result) {

            isLoading.setValue(false);

            if (result != null) {
                repository.setValue(result);
            }
            else {
                isNetworkException.setValue(true);
            }
        }
    }

}
