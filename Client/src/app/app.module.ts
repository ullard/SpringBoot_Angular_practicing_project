import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { RouterModule, ExtraOptions }   from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { CookieService } from 'ngx-cookie-service';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { RegistrationComponent } from './registration/registration.component';
import { RegistrationConfirmComponent } from './registration-confirm/registration-confirm.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { PasswordChangeComponent } from './password-change/password-change.component';
import { ProfileComponent } from './profile/profile.component';
import { SettingsComponent } from './settings/settings.component';
import { SettingsTfaComponent } from './settings-tfa/settings-tfa.component';
import { SettingsPassChangeComponent } from './settings-pass-change/settings-pass-change.component';
import { TfaComponent } from './tfa/tfa.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RegistrationComponent,
    RegistrationConfirmComponent,
    PasswordResetComponent,
    PasswordChangeComponent,
    ProfileComponent,
    SettingsComponent,
    SettingsTfaComponent,
    SettingsPassChangeComponent,
    TfaComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    HttpClientXsrfModule,
    AppRoutingModule,
    RouterModule.forRoot([
		{ path: '', component: HomeComponent },
		{ path: 'login', component: LoginComponent },
		{ path: 'registration', component: RegistrationComponent},
		{ path: 'registrationConfirm', component: RegistrationConfirmComponent},
		{ path: 'passwordReset', component: PasswordResetComponent},
		{ path: 'passwordChange', component: PasswordChangeComponent},
		{ path: 'profile/:username', component: ProfileComponent},
		{ path: 'settings', component: SettingsComponent},
		{ path: 'settings/tfa', component: SettingsTfaComponent},
		{ path: 'settings/passChange', component: SettingsPassChangeComponent},
		{ path: 'login/tfa', component: TfaComponent},
		
		{ path: '**', redirectTo: '' },
		], {onSameUrlNavigation: 'reload'}
    )],
  providers: [CookieService],
  bootstrap: [AppComponent],
  exports: [RouterModule]
})
export class AppModule { }
