package dev.colibri.githubclienttest.di;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import android.arch.persistence.room.Room;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dev.colibri.githubclienttest.db.AppDatabase;
import dev.colibri.githubclienttest.db.RepositoryDao;
import dev.colibri.githubclienttest.network.GithubService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context getContext() {
        return context;
    }

    @Provides
    @Singleton
    public AppDatabase providesAppDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();

    }

    @Provides
    @Singleton
    public RepositoryDao providesRepositoryDao(AppDatabase appDatabase) {
        return appDatabase.repositoryDao();

    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(GithubService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Provides
    @Singleton
    public GithubService providesGithubService(Retrofit retrofit) {
        return retrofit.create(GithubService.class);

    }

    @Provides
    @Singleton
    Executor providesExecutor() {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(numberOfCores);
    }

}
