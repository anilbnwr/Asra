/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.asra.mobileapp.dagger.builder;


import com.asra.mobileapp.MainActivity;
import com.asra.mobileapp.ui.admin.AdminActivity;
import com.asra.mobileapp.ui.dashboard.DashboardActivity;
import com.asra.mobileapp.ui.general.ETFragmentHostActivity;
import com.asra.mobileapp.ui.general.policy.PolicyActivity;
import com.asra.mobileapp.ui.guest.GuestActivity;
import com.asra.mobileapp.ui.login.LoginActivity;
import com.asra.mobileapp.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract AdminActivity bindAdminActivity();

    @ContributesAndroidInjector
    abstract GuestActivity bindGuestActivity();

    @ContributesAndroidInjector
    abstract DashboardActivity bindDashboardActivity();

    @ContributesAndroidInjector
    abstract ETFragmentHostActivity bindETFragmentHostActivity();

    @ContributesAndroidInjector
    abstract PolicyActivity bindETFragmentPolicyActivity();
}
