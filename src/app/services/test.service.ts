import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Test } from '../models/test.model';
import { Task } from '../models/task.model';
import { AnswerOption } from '../models/answerOption.model';

const baseUrl = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient) { }

  getAllTest(idDiscipline: any): Observable<Test[]> {
    return this.http.get<Test[]>(`${baseUrl}/${idDiscipline}/tests`)
  }

  getTestById(idDiscipline: any, idTest: any): Observable<Test> {
    return this.http.get<Test>(`${baseUrl}/${idDiscipline}/tests/${idTest}`)
  }

  createTest(idDiscipline: any, data: any): Observable<any> {
    return this.http.post(`${baseUrl}/${idDiscipline}/tests`, data)
  }

  updateTest(idDiscipline: any, idTest: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${idDiscipline}/tests/${idTest}`, data)
  }

  deleteTest(idDiscipline: any, idTest: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${idDiscipline}/tests/${idTest}`)
  }



  getAllTask(idDiscipline: any, idTest: any): Observable<Task[]> {
    return this.http.get<Task[]>(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks`)
  }

  getTaskById(idDiscipline: any, idTest: any, idTask: any): Observable<Task> {
    return this.http.get<Task>(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}`)
  }

  createTask(idDiscipline: any, idTest: any, data: any): Observable<any> {
    return this.http.post(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks`, data)
  }

  updateTask(idDiscipline: any, idTest: any, idTask: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}`, data)
  }

  deleteTask(idDiscipline: any, idTest: any, idTask: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}`)
  }



  getAllAnswerOption(idDiscipline: any, idTest: any, idTask: any): Observable<AnswerOption[]> {
    return this.http.get<AnswerOption[]>(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}/answerOptions`)
  }

  getAllAnswerOptionInTest(idDiscipline: any, idTest: any, idTask: any): Observable<AnswerOption[]> {
    return this.http.get<AnswerOption[]>(` ${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}/allAnswerOptions`)
  }

  getAnswerOptionById(idDiscipline: any, idTest: any, idTask: any, idAnswerOption: any): Observable<AnswerOption> {
    return this.http.get<AnswerOption>(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}/answerOptions/${idAnswerOption}`)
  }

  createAnswerOption(idDiscipline: any, idTest: any, idTask: any, data: any): Observable<any> {
    return this.http.post(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}/answerOptions`, data)
  }

  updateAnswerOption(idDiscipline: any, idTest: any, idTask: any, idAnswerOption: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}/answerOptions/${idAnswerOption}`, data)
  }

  deleteAnswerOption(idDiscipline: any, idTest: any, idTask: any, idAnswerOption: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${idDiscipline}/tests/${idTest}/tasks/${idTask}/answerOptions/${idAnswerOption}`)
  }
}
