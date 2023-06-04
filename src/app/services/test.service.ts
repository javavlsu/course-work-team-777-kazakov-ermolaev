import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Test } from '../models/test.model';

const baseUrl = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient) { }

  getAll(idDiscipline: any): Observable<Test[]> {
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
}
