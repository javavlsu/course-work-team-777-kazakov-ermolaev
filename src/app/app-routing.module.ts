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
import { GroupListComponent } from './components/group-list/group-list.component';
import { GroupDetailsComponent } from './components/group-details/group-details.component';
import { DisciplinePageComponent } from './components/discipline-page/discipline-page.component';
import { TestPageComponent } from './components/test-page/test-page.component';
import { TestEditComponent } from './components/test-edit/test-edit.component';
import { LabworkEditComponent } from './components/labwork-edit/labwork-edit.component';
import { LabworkPageComponent } from './components/labwork-page/labwork-page.component';
import { AnswerOptionPageComponent } from './components/answer-option-page/answer-option-page.component';

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
  { path: 'users/lectors/:idLector', component: UserDetailsComponent },
  { path: 'groups', component: GroupListComponent },
  { path: 'groups/:idGroup', component: GroupDetailsComponent },
  { path: 'discipline/:idDiscipline/page', component: DisciplinePageComponent },
  { path: 'discipline/:idDiscipline/test/:idTest', component: TestEditComponent },
  { path: 'discipline/:idDiscipline/testPage/:idTest', component: TestPageComponent },
  { path: 'discipline/:idDiscipline/testPage/:idTest/task/:idTask/answer', component: AnswerOptionPageComponent },
  { path: 'discipline/:idDiscipline/labwork/:idLabWork', component: LabworkEditComponent },
  { path: 'discipline/:idDiscipline/labworkPage/:idLabWork', component: LabworkPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
