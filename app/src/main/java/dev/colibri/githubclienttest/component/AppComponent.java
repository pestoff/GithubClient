package dev.colibri.githubclienttest.component;

import dagger.Component;
import dev.colibri.githubclienttest.fragment.RepoDetailsFragment;
import dev.colibri.githubclienttest.fragment.RepoListFragment;
import dev.colibri.githubclienttest.module.ViewModelModule;

@Component (modules = ViewModelModule.class)
public interface AppComponent {
    void inject(RepoListFragment repoListFragment);
    void inject(RepoDetailsFragment repoDetailsFragment);
}
