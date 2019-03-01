package dev.colibri.githubclienttest.di;

import android.arch.lifecycle.ViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dev.colibri.githubclienttest.viewmodel.RepoDetailsViewModel;
import dev.colibri.githubclienttest.viewmodel.RepoListViewModel;

@Module
public class ViewModelModule {

    @IntoMap
    @Provides
    @ViewModelKey(RepoListViewModel.class)
    public ViewModel repoListViewModel(RepoListViewModel repoListViewModel) {
        return repoListViewModel;
    }

    @IntoMap
    @Provides
    @ViewModelKey(RepoDetailsViewModel.class)
    public ViewModel repoDetailsViewModel(RepoDetailsViewModel repoDetailsViewModel) {
        return repoDetailsViewModel;
    }
}