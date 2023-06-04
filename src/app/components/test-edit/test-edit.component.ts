import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Test } from 'src/app/models/test.model';
import { StorageService } from 'src/app/services/storage.service';
import { TestService } from 'src/app/services/test.service';

@Component({
  selector: 'app-test-edit',
  templateUrl: './test-edit.component.html',
  styleUrls: ['./test-edit.component.css']
})
export class TestEditComponent {
  private idTest = this.route.snapshot.params["idTest"];
  private idDiscipline = this.route.snapshot.params["idDiscipline"];

  test: Test = {
    title: "",
    dateStart: new Date(),
    deadline: new Date(),
    status: ""
  }

  isUpdateFailed = false;
  errorMessage = "";

  constructor(
    private testService: TestService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

    ngOnInit() {
      this.getTest();
    }

    getTest() {
      this.testService.getTestById(this.idDiscipline, this.idTest)
        .subscribe({
          next: (data) => {
            this.test = data;
          },
          error: (e) => console.error(e)
        });
    }

    saveTest() {
      const data = {
        title: this.test.title,
        dateStart: this.test.dateStart,
        deadline: this.test.deadline
      }

      this.testService.updateTest(this.idDiscipline, this.idTest, data)
        .subscribe({
          next: (res) => {
            console.log(res);
            this.isUpdateFailed = false;
            this.redirectToDiscipline();

          },
          error: (e) => {
            this.errorMessage = e.error.message;
            this.isUpdateFailed = true;
          }
        })
    }

    redirectToDiscipline() {
      this.router.navigate([`/discipline/${this.idDiscipline}/page`])
    }
}
