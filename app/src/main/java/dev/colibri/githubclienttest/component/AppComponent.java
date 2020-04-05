package dev.colibri.githubclienttest.component;

import javax.inject.Singleton;

import dagger.Component;
import dev.colibri.githubclienttest.fragment.RepoDetailsFragment;
import dev.colibri.githubclienttest.fragment.RepoListFragment;
import dev.colibri.githubclienttest.module.AppModule;
import dev.colibri.githubclienttest.module.ViewModelModule;

@Component (modules = {AppModule.class, ViewModelModule.class})
@Singleton
public interface AppComponent {
    void inject(RepoListFragment repoListFragment);
    void inject(RepoDetailsFragment repoDetailsFragment);
}
