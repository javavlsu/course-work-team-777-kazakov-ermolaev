import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { httpInterceptorProviders } from './_helpers/auth.interceptor';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from "@ng-select/ng-select";
import { HttpClientModule } from '@angular/common/http';
import { DisciplineListComponent } from './components/discipline-list/discipline-list.component';
import { AddDisciplineComponent } from './components/add-discipline/add-discipline.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import { DisciplineDetailsComponent } from './components/discipline-details/discipline-details.component';
import { GroupListComponent } from './components/group-list/group-list.component';
import { GroupDetailsComponent } from './components/group-details/group-details.component';
import { DisciplinePageComponent } from './components/discipline-page/discipline-page.component';
import { TestEditComponent } from './components/test-edit/test-edit.component';
import { TestPageComponent } from './components/test-page/test-page.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    DisciplineListComponent,
    AddDisciplineComponent,
    UserListComponent,
    UserDetailsComponent,
    DisciplineDetailsComponent,
    GroupListComponent,
    GroupDetailsComponent,
    DisciplinePageComponent,
    TestEditComponent,
    TestPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgSelectModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
