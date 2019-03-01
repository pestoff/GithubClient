package dev.colibri.githubclienttest.di;

import javax.inject.Singleton;

import dagger.Component;
import dev.colibri.githubclienttest.fragment.RepoDetailsFragment;
import dev.colibri.githubclienttest.fragment.RepoListFragment;

@Singleton
@Component(modules = ViewModelModule.class)
public interface AppComponent {

    void inject(RepoListFragment repoListFragment);
    void inject(RepoDetailsFragment repoDetailsFragment);
}
