import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';
import { RegisterComponent } from './components/register/register.component';
import { DisciplineListComponent } from './components/discipline-list/discipline-list.component';
import { AddDisciplineComponent } from './components/add-discipline/add-discipline.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import { DisciplineDetailsComponent } from './components/discipline-details/discipline-details.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'newuser', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '', redirectTo: 'discipline', pathMatch: 'full' },
  { path: 'discipline', component: DisciplineListComponent},
  { path: 'discipline/addDiscipline', component: AddDisciplineComponent },
  { path: 'discipline/:idDiscipline', component: DisciplineDetailsComponent},
  { path: 'users', component: UserListComponent },
  { path: 'users/students/:idStudent', component: UserDetailsComponent },
  { path: 'users/lectors/:idLector', component: UserDetailsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
