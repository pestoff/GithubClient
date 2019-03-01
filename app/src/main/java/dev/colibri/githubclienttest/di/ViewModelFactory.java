package dev.colibri.githubclienttest.di;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {
    private Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelMap;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelMap) {
        this.viewModelMap = viewModelMap;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<ViewModel> provider = viewModelMap.get(modelClass);

        if(provider == null){
            throw new IllegalArgumentException(
                    "Объект " + modelClass.getSimpleName() + " не может быть создан");
        }
        return (T) provider.get();
    }
}