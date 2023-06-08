import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LabWork } from '../models/labwork.model';
import { Observable } from 'rxjs';
import { Student } from '../models/student.model';

const baseUrl = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class LabworkService {

  constructor(private http: HttpClient) { }

  getAll(idDiscipline: any): Observable<LabWork[]> {
    return this.http.get<LabWork[]>(`${baseUrl}/${idDiscipline}/labworks`)
  }

  getLabWorkById(idDiscipline: any, idLabwork: any): Observable<LabWork> {
    return this.http.get<LabWork>(`${baseUrl}/${idDiscipline}/labworks/${idLabwork}`)
  }

  createLabWork(idDiscipline: any, data: any): Observable<any> {
    return this.http.post(`${baseUrl}/${idDiscipline}/labworks`, data)
  }

  updateLabWork(idDiscipline: any, idLabwork: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${idDiscipline}/labworks/${idLabwork}`, data)
  }

  deleteLabWork(idDiscipline: any, idLabwork: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${idDiscipline}/labworks/${idLabwork}`)
  }

  getScore(idDiscipline: any, idLabwork: any): Observable<Student[]> {
    return this.http.get<Student[]>(`${baseUrl}/${idDiscipline}/labworks/${idLabwork}/scoreStudentForLabWork`);
  }

  updateScoreLabWork(idLabWork: any, idDiscipline: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${idDiscipline}/labworks/${idLabWork}/putScore`, data);
  }

  createScoreLabWork(idDiscipline: any, idLabWork: any): Observable<any> {
    return this.http.get(`${baseUrl}/${idDiscipline}/labworks/${idLabWork}/createScoreLabWork`)
  }
}
