import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Task } from 'src/app/models/task.model';
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

  task: Task = {
    title: "",
    editTask: false
  }

  tasks: Task[] = [];

  isSaveTaskFailed = false;
  isUpdateTaskFailed = false;
  isUpdateFailed = false;
  errorMessage = "";
  taskOpen = false;
  isCreateTask = false;

  constructor(
    private testService: TestService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

    ngOnInit() {
      this.getTest();
      this.getTask();
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

    openTasks() {
      this.taskOpen = true;
    }

    closeTasks() {
      this.taskOpen = false;
    }

    getTask() {
      this.testService.getAllTask(this.idDiscipline, this.idTest)
        .subscribe({
          next:(data) => {
            this.tasks = data;
          },
          error: (e) => console.error(e)
        })
    }

    createTask() {
      const data = {
        title: this.task.title
      }

      this.testService.createTask(this.idDiscipline, this.idTest, data)
        .subscribe({
          next: () => {
            this.isSaveTaskFailed = false;
            this.getTask();
            this.task = {
              title: ""
            }
          },
          error: (e) => {
            this.errorMessage = e.error.message;
            this.isSaveTaskFailed = true;
          }
        })
    }

    updateTask(title: string, idTask: any) {
      const data = {
        title: title
      }

      console.log(data)

      this.testService.updateTask(this.idDiscipline, this.idTest, idTask, data)
        .subscribe({
          next: () => {
            this.isUpdateTaskFailed = false;
            this.task.editTask = false;
            this.getTask();
          },
          error: (e) => {
            this.errorMessage = e.error.message;
            this.isUpdateTaskFailed = true;
          }
        })
    }

    deleteTask(id: any) {
      this.testService.deleteTask(this.idDiscipline, this.idTest, id)
        .subscribe({
          next: () => this.getTask(),
          error: (e) => console.error(e)
        })
    }

    redirectToAnswerPage(id: any) {

    }
}
