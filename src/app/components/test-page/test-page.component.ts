import { TestService } from 'src/app/services/test.service';
import { Component } from '@angular/core';
import { Test } from 'src/app/models/test.model';
import { StorageService } from 'src/app/services/storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentTest } from 'src/app/models/studentTest.model';
import { Student } from 'src/app/models/student.model';

@Component({
  selector: 'app-test-page',
  templateUrl: './test-page.component.html',
  styleUrls: ['./test-page.component.css']
})
export class TestPageComponent {
  private idTest = this.route.snapshot.params["idTest"];
  private idDiscipline = this.route.snapshot.params["idDiscipline"];
  private roles: string[] = [];

  test: Test = {
    title: "",
    dateStart: new Date(),
    deadline: new Date(),
    status: ""
  }

  studentTest: StudentTest = {};
  students: Student[] = [];

  isLoggedIn = false;
  isAdmin = false;
  isLector = false;

  listOpen = false
  isOpen = false;
  isScore = false;
  errorMess = "";

  constructor(
    private testService: TestService,
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
    this.getScore();
    this.getScores();
  }

  getTest() {
    this.testService.getTestById(this.idDiscipline, this.idTest)
      .subscribe({
        next: (data) => {
          this.test = data;
          if (data.status?.includes("open")) {
            this.isOpen = true;
          }
          if (data.status?.includes("close")) {
            this.isOpen = false;
          }
        },
        error: (e) => console.error(e)
      });
  }

  getScore() {
    this.testService.getScoreStudentForTest(this.idDiscipline, this.idTest)
      .subscribe({
        next: (data) => {
          this.studentTest = data;
          this.isScore = true;
        },
        error: (e) => {
          this.isScore = false;
        }
      })
  }

  getScores() {
    this.testService.getScores(this.idDiscipline, this.idTest)
    .subscribe({
      next: (data) => this.students = data,
      error: (e) => console.error(e)
    })
  }

  redirectToDiscipline() {
    this.router.navigate([`/discipline/${this.idDiscipline}/page`])
  }

  redirectToTestPass() {
    this.router.navigate([`/discipline/${this.idDiscipline}/testpage/${this.idTest}/testPass`])
  }

  giveMoreChance(student: Student) {
    const data = {
      id: student.id
    }
    this.testService.getOneMoreChance(this.idDiscipline, this.idTest, data)
      .subscribe({
        next: () => this.getScores(),
        error: (e) => console.error(e)
      });
  }
}
