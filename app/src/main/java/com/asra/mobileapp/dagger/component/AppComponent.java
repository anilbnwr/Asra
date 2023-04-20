package com.asra.mobileapp.dagger.component;

import android.app.Application;


import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.dagger.builder.ActivityBuilder;
import com.asra.mobileapp.dagger.builder.FragmentBuilderModule;
import com.asra.mobileapp.dagger.module.AppModule;
import com.asra.mobileapp.dagger.module.DbModule;
import com.asra.mobileapp.dagger.module.EvolveApiServiceModule;
import com.asra.mobileapp.dagger.module.EvolveNetworkAdapterModule;
import com.asra.mobileapp.dagger.module.NetworkModule;
import com.asra.mobileapp.dagger.module.ServiceModule;
import com.asra.mobileapp.dagger.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


@Singleton
@Component(modules = {
        AppModule.class,
        AndroidInjectionModule.class,
        FragmentBuilderModule.class,
        ActivityBuilder.class,
        NetworkModule.class,
        DbModule.class,
        EvolveApiServiceModule.class,
        EvolveNetworkAdapterModule.class,
        ServiceModule.class,
        ViewModelModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();


    }

    void inject(ETApplication aplApplication);
}