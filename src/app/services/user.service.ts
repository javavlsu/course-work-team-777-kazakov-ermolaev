import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Lector } from '../models/lector.model';
import { Student } from '../models/student.model';

const baseUrl = 'http://localhost:8080/api/users';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http: HttpClient) { }

  getStudent(): Observable<Student[]> {
    return this.http.get<Student[]>(`${baseUrl}/students`);
  }

  getStudentById(id: any): Observable<Student> {
    return this.http.get<Student>(`${baseUrl}/students/${id}`);
  }

  updateStudent(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/students/${id}`, data);
  }

  deleteStudent(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/students/${id}`);
  }

  getLector(): Observable<Lector[]> {
    return this.http.get<Lector[]>(`${baseUrl}/lectors`);
  }

  getLectorById(id: any): Observable<Lector> {
    return this.http.get<Lector>(`${baseUrl}/lectors/${id}`);
  }

  updateLector(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/lectors/${id}`, data);
  }

  deleteLector(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/lectors/${id}`);
  }
}
