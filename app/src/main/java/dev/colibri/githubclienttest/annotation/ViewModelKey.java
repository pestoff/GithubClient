package dev.colibri.githubclienttest.annotation;

import android.arch.lifecycle.ViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import dagger.MapKey;

import static java.lang.annotation.RetentionPolicy.*;

@Target(ElementType.METHOD)
@Retention(RUNTIME)
@MapKey
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}