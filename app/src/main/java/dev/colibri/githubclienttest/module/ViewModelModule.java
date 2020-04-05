package dev.colibri.githubclienttest.module;

import android.arch.lifecycle.ViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dev.colibri.githubclienttest.annotation.ViewModelKey;
import dev.colibri.githubclienttest.viewModel.RepoDetailsViewModel;
import dev.colibri.githubclienttest.viewModel.RepoListViewModel;

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
