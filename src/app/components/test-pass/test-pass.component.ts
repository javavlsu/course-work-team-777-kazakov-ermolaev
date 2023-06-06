import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AnswerOption } from 'src/app/models/answerOption.model';
import { Task } from 'src/app/models/task.model';
import { Test } from 'src/app/models/test.model';
import { StorageService } from 'src/app/services/storage.service';
import { TestService } from 'src/app/services/test.service';

@Component({
  selector: 'app-test-pass',
  templateUrl: './test-pass.component.html',
  styleUrls: ['./test-pass.component.css']
})
export class TestPassComponent {
  private idDiscipline = this.route.snapshot.params["idDiscipline"];
  private idTest = this.route.snapshot.params["idTest"];

  constructor(
    private testService: TestService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

  test: Test = {};
  tasks: Task[] = [];
  answerOptions: AnswerOption[] = [];


  ngOnInit() {
    this.getTest();
    this.getTask();
    this.getAnswer();
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

  getTask() {
    this.testService.getAllTask(this.idDiscipline, this.idTest)
      .subscribe({
        next:(data) => {
          this.tasks = data;
        },
        error: (e) => console.error(e)
      })
  }

  getAnswer() {
    this.testService.getAllAnswerOptionInTest(this.idDiscipline, this.idTest, 1)
      .subscribe({
        next:(data) => {
          this.answerOptions = data;
          console.log(this.answerOptions)
        },
        error: (e) => console.error(e)
      })
  }

  isOnlyOneAnswer(task: Task): Boolean {
    if (task.type?.includes("radio")) {
      return true;
    }
    else {
      return false;
    }
  }

  answerInTask(idTask: any, idAnswer: any): Boolean{
    return idTask == idAnswer;
  }
}
