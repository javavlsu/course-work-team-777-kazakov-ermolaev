import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, FormControl } from '@angular/forms';
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

  form: FormGroup;

  constructor(
    private testService: TestService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router,
    fb: FormBuilder) {
      this.form = fb.group({
        pickedAnswers:  new FormArray([])
       });
    }

  test: Test = {};
  tasks: Task[] = [];
  answerOptions: AnswerOption[] = [];

  selectedAnswerOption: AnswerOption[] = [];

  testCheaked = false;
  errorMess = "";

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
    this.testService.getAllAnswerOptionInTest(this.idDiscipline, this.idTest)
      .subscribe({
        next:(data) => {
          this.answerOptions = data
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

  cheakTest() {
    console.log(this.selectedAnswerOption)
    const data = {
      answers: this.selectedAnswerOption
    }
    this.testService.cheakTest(this.idDiscipline, this.idTest, data)
      .subscribe({
        next: () => {
          this.testCheaked = true;
          this.router.navigate([`/discipline/${this.idDiscipline}/testPage/${this.idTest}`])
        },
        error: (e) => {
          this.testCheaked = false;
          this.errorMess =  e.error.message;
        }
      })
  }

  onCheckboxChange(event: any, answer: AnswerOption) {

    const pickedAnswers = (this.form.controls['pickedAnswers'] as FormArray);
    if (event.target.checked) {
      pickedAnswers.push(new FormControl(event.target.value));
      this.selectedAnswerOption.push(answer);
    } else {
      const index = pickedAnswers.controls
      .findIndex(x => x.value == event.target.value);
      pickedAnswers.removeAt(index);
      this.selectedAnswerOption.forEach( (item, index) => {
        if(item == answer) this.selectedAnswerOption.splice(index, 1);
      });
    }
  }

  onRadioChange(event: any, answer: AnswerOption) {
    const pickedAnswers = (this.form.controls['pickedAnswers'] as FormArray);
    if (event.target.checked) {
      pickedAnswers.push(new FormControl(event.target.value));
      this.selectedAnswerOption.push(answer);
    } else {
      const index = pickedAnswers.controls
      .findIndex(x => x.value == event.target.value);
      pickedAnswers.removeAt(index);
      this.selectedAnswerOption.forEach( (item, index) => {
        if(item == answer) this.selectedAnswerOption.splice(index, 1);
      });
    }
  }
}
