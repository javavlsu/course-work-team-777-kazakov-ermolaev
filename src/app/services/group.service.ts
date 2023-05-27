import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Group } from '../models/group.model';

const baseUrl = 'http://localhost:8080/api/users/students/groups';
@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Group[]> {
    return this.http.get<Group[]>(baseUrl);
  }

  createGroup(data: any): Observable<any> {
    return this.http.post(baseUrl, data)
  }

  updateGroup(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${id}`, data);
  }

  deleteGroup(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }
}
