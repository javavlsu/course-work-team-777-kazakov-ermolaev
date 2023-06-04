import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Discipline } from 'src/app/models/discipline.model';
import { Test } from 'src/app/models/test.model';
import { DisciplineService } from 'src/app/services/discipline.service';
import { StorageService } from 'src/app/services/storage.service';
import { TestService } from 'src/app/services/test.service';

@Component({
  selector: 'app-discipline-page',
  templateUrl: './discipline-page.component.html',
  styleUrls: ['./discipline-page.component.css']
})
export class DisciplinePageComponent {
  private idDiscipline = this.route.snapshot.params["idDiscipline"];
  private roles: string[] = [];

  discipline: Discipline = {
    title: ""
  }

  test: Test = {
    title: "",
    dateStart: new Date(),
    deadline: new Date(),
  }

  tests: Test[] = [];

  isLoggedIn = false;
  isAdmin = false;
  isLector = false;

  createForm = false;
  isCreateFailed = false;
  errorMessage = "";

  constructor(
    private testService: TestService,
    private disciplineService: DisciplineService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isLector = this.roles.includes('ROLE_LECTOR');
    }

    this.getTest();
    this.getDiscipline();
  }

  getDiscipline() {
    this.disciplineService.getById(this.idDiscipline)
      .subscribe({
        next: (data) => {
          this.discipline = data;
        },
        error: (e) => console.error(e)
      });
  }

  getTest() {
    this.testService.getAll(this.idDiscipline)
      .subscribe({
        next: (data) => {
          this.tests = data;
        },
        error: (e) => console.error(e)
      })
  }

  redirectToTestEdit(idTest: any) {
    this.router.navigate([`/discipline/${this.idDiscipline}/test/${idTest}`])
  }

  redirectToTestPage(idTest: any) {
    this.router.navigate([`/discipline/${this.idDiscipline}/testPage/${idTest}`])
  }

  redirectToListDiscipline() {
    this.router.navigate([`discipline`])
  }

  deleteTest(idTest: any) {
    this.testService.deleteTest(this.idDiscipline, idTest)
      .subscribe({
        next: (data) => {
          this.getTest();
        },
        error: (e) => console.error(e)
      })
  }

  openCreateForm() {
    this.createForm = true;
  }

  closeCreateForm() {
    this.createForm = false;
  }

  saveTest() {
    const data = {
      title: this.test.title,
      dateStart: this.test.dateStart,
      deadline: this.test.deadline
    }

    this.testService.createTest(this.idDiscipline, data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.isCreateFailed = false;
          this.test = {
            title: "",
            deadline: new Date(),
            status: ""
          }
          this.getTest();
        },
        error: (e) => {
          this.errorMessage = e.error.message;
          this.isCreateFailed = true;
        }
      })
  }
}
