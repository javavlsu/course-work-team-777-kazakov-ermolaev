import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AnswerOption } from 'src/app/models/answerOption.model';
import { Task } from 'src/app/models/task.model';
import { StorageService } from 'src/app/services/storage.service';
import { TestService } from 'src/app/services/test.service';

@Component({
  selector: 'app-answer-option-page',
  templateUrl: './answer-option-page.component.html',
  styleUrls: ['./answer-option-page.component.css']
})
export class AnswerOptionPageComponent {
  private idDiscipline = this.route.snapshot.params["idDiscipline"];
  private idTest = this.route.snapshot.params["idTest"];
  private idTask = this.route.snapshot.params["idTask"];

  constructor(
    private testService: TestService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

  answerOption: AnswerOption = {
    title: "",
    isRight: ""
  }

  task: Task = {}
  answerOptions: AnswerOption[] = [];
  createForm = false;


  isUpdateFailed = false;
  isCreateFailed = false;
  isCreated = false;
  isUpdated = false;
  currentMess = "";
  errorMessage = "";

  ngOnInit() {
    this.getAnswer();
    this.getTask();
  }

  getTask() {
    this.testService.getTaskById(this.idDiscipline, this.idTest, this.idTask)
      .subscribe({
        next:(data) => {
          this.task = data;
        },
        error: (e) => console.error(e)
      })
  }

  getAnswer() {
    this.testService.getAllAnswerOption(this.idDiscipline, this.idTest, this.idTask)
      .subscribe({
        next:(data) => {
          this.answerOptions = data;
        },
        error: (e) => console.error(e)
      })
  }

  getAnswerById(id: any) {
    this.testService.getAnswerOptionById(this.idDiscipline, this.idTest, this.idTask, id)
      .subscribe({
        next: (data) => {
          console.log(data)
        }
      })
  }

  isRight(isRight: string): Boolean {
    if (isRight.includes("true")) {
      return true;
    }
    else {
      return false;
    }
  }

  changeIsRight(answer: AnswerOption) {
    if (answer.isRight?.includes("true")) {
      answer.isRight = "false";
    }
    else {
      answer.isRight = "true";
    }
  }

  createAnswer() {
    const data = {
      title: this.answerOption.title,
      isRight: this.answerOption.isRight
    }

    this.testService.createAnswerOption(this.idDiscipline, this.idTest, this.idTask, data)
      .subscribe({
        next: (data) => {
          this.isCreateFailed = false;
          this.currentMess = data.message;
          this.isCreated = true;
          this.getAnswer();
        },
        error: (e) => {
          this.errorMessage = e.error.message;
          this.isCreateFailed = true;
          this.isCreated = false;
        }
      })
  }

  deleteAnswer(id: any) {
    this.testService.deleteAnswerOption(this.idDiscipline, this.idTest, this.idTask, id)
    .subscribe({
      next:() => this.getAnswer(),
      error: (e) => console.error(e)
    })
  }

  saveAnswer() {
    console.log(this.answerOptions)
    const data = {
      answers: this.answerOptions
    }

    this.testService.updateAnswerOption(this.idDiscipline, this.idTest, this.idTask, 2, data)
      .subscribe({
        next: (res) => {
          this.isUpdateFailed = false;
          this.currentMess = res.message;
          this.isUpdated = true;
        },
        error: (e) => {
          this.errorMessage = e.error.message;
          this.isUpdateFailed = true;
          this.isUpdated = false;
        }
      })
  }
  redirectToTestEdit() {
    this.router.navigate([`/discipline/${this.idDiscipline}/test/${this.idTest}`])
  }
}
