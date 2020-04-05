package dev.colibri.githubclienttest.app;

import android.app.Application;

import dev.colibri.githubclienttest.component.AppComponent;
import dev.colibri.githubclienttest.component.DaggerAppComponent;
import dev.colibri.githubclienttest.module.AppModule;


public class App extends Application {
    private static App instance;
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(instance))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
