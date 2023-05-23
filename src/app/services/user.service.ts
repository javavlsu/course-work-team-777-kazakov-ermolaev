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

  constructor( private http: HttpClient) { }

  getLector(): Observable<Lector[]> {
    return this.http.get<Lector[]>(`${baseUrl}/lectors`);
  }

  getStudent(): Observable<Student[]> {
    return this.http.get<Student[]>(`${baseUrl}/students`);
  }

}
