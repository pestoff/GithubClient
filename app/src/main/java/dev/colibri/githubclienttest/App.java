package dev.colibri.githubclienttest;

import android.app.Application;
import android.arch.persistence.room.Room;

import dev.colibri.githubclienttest.db.AppDatabase;
import dev.colibri.githubclienttest.di.AppComponent;
import dev.colibri.githubclienttest.di.DaggerAppComponent;
import dev.colibri.githubclienttest.network.HttpClient;
import dev.colibri.githubclienttest.repository.DataRepository;

public class App extends Application {
    private static HttpClient httpClient;
    private static AppDatabase appDatabase;
    private static DataRepository dataRepository;
    private static AppComponent appComponent;
    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        appComponent = DaggerAppComponent.create();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static DataRepository getDataRepository() {
        if(dataRepository == null) {
            dataRepository = new DataRepository();
        }

        return dataRepository;
    }

    public static HttpClient getHttpClient() {
        if(httpClient == null) {
            httpClient = new HttpClient();
        }
        return httpClient;
    }

    public static AppDatabase getAppDatabase() {
        if(appDatabase == null) {
            appDatabase = Room.databaseBuilder(INSTANCE, AppDatabase.class, AppDatabase.DB_NAME).build();
        }
        return appDatabase;
    }
}

