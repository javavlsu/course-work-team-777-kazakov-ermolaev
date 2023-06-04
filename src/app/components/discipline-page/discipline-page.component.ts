import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Discipline } from 'src/app/models/discipline.model';
import { LabWork } from 'src/app/models/labwork.model';
import { Test } from 'src/app/models/test.model';
import { DisciplineService } from 'src/app/services/discipline.service';
import { LabworkService } from 'src/app/services/labwork.service';
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

  createFormTest = false;
  isCreateTestFailed = false;
  errorMessageTest = "";

  labWork: LabWork = {
    title: "",
    manual: "",
    deadline: new Date()
  }

  labworks: LabWork[] = [];

  createFormLabWork = false;
  isCreateLabWorkFailed = false;
  errorMessageLabWork = "";

  constructor(
    private testService: TestService,
    private labworkService: LabworkService,
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
    this.getLabWork();
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
        next: () => {
          this.getTest();
        },
        error: (e) => console.error(e)
      })
  }

  openCreateFormTest() {
    this.createFormTest = true;
  }

  closeCreateFormTest() {
    this.createFormTest = false;
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
          this.isCreateTestFailed = false;
          this.test = {
            title: "",
            deadline: new Date(),
            status: ""
          }
          this.getTest();
        },
        error: (e) => {
          this.errorMessageTest = e.error.message;
          this.isCreateTestFailed = true;
        }
      })
  }

  getLabWork() {
    this.labworkService.getAll(this.idDiscipline)
      .subscribe({
        next: (data) => {
          this.labworks = data;
        },
        error: (e) => console.error(e)
      })
  }

  redirectToLabWorkEdit(idLabWork: any) {
    this.router.navigate([`/discipline/${this.idDiscipline}/labwork/${idLabWork}`])
  }

  redirectToLabWorkPage(idLabWork: any) {
    this.router.navigate([`/discipline/${this.idDiscipline}/labworkPage/${idLabWork}`])
  }

  deleteLabWork(idLabWork: any) {
    this.labworkService.deleteLabWork(this.idDiscipline, idLabWork)
      .subscribe({
        next: () => {
          this.getLabWork();
        },
        error: (e) => console.error(e)
      })
  }

  openCreateFormLabWork() {
    this.createFormLabWork = true;
  }

  closeCreateFormLabWork() {
    this.createFormLabWork = false;
  }

  saveLabWork() {
    const data = {
      title: this.labWork.title,
      manual: this.labWork.manual,
      deadline: this.labWork.deadline
    }

    this.labworkService.createLabWork(this.idDiscipline, data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.isCreateLabWorkFailed = false;
          this.labWork = {
            title: "",
            manual: "",
            deadline: new Date()
          }
          this.getLabWork();
        },
        error: (e) => {
          this.errorMessageLabWork = e.error.message;
          this.isCreateLabWorkFailed = true;
        }
      })
  }
}
