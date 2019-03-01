package dev.colibri.githubclienttest;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import dev.colibri.githubclienttest.entity.Owner;
import dev.colibri.githubclienttest.entity.Repository;

public class Constants {
    public static final Repository REPOSITORY = new Repository(
            1,
            "name",
            "description",
            "2019-01-01T04:02:57Z",
            "2018-11-05T04:02:57Z",
            100,
            "en",
            100,
            new Owner("login", 1, "https://sample/sample.png")
    );

    public static final List<Repository> REPOSITORIES = Collections.singletonList(REPOSITORY);
    public static Executor TEST_EXECUTOR = command -> command.run();

}
