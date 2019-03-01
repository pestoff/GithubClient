package dev.colibri.githubclienttest;

import android.app.Application;

import dev.colibri.githubclienttest.di.AppComponent;
import dev.colibri.githubclienttest.di.AppModule;
import dev.colibri.githubclienttest.di.DaggerAppComponent;

public class App extends Application {
    private static AppComponent appComponent;
    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        appComponent = DaggerAppComponent.builder()
                                         .appModule(new AppModule(this))
                                         .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}

