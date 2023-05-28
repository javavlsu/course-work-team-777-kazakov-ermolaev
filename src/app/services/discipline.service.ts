import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Discipline } from '../models/discipline.model';
import { Group } from '../models/group.model';
import { Lector } from '../models/lector.model';

const baseUrl = 'http://localhost:8080/api/discipline';

@Injectable({
  providedIn: 'root'
})
export class DisciplineService {

  constructor( private http: HttpClient) { }

  getAll(): Observable<Discipline[]> {
    return this.http.get<Discipline[]>(baseUrl)
  }

  getById(id: any): Observable<Discipline> {
    return this.http.get<Discipline>(`${baseUrl}/${id}`)
  }

  create(data: any): Observable<any> {
    return this.http.post(baseUrl, data)
  }

  update(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${id}`, data);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  getGroupInByDisciplineId(id: any): Observable<Group[]> {
    return this.http.get<Group[]>(`${baseUrl}/groups/withDiscipline/${id}`);
  }

  getGroupOutByDisciplineId(id: any): Observable<Group[]> {
    return this.http.get<Group[]>(`${baseUrl}/groups/withoutDiscipline/${id}`);
  }

  getLectorsInByDisciplineId(id: any): Observable<Lector[]> {
    return this.http.get<Lector[]>(`${baseUrl}/lector/withDiscipline/${id}`);
  }

  getLectorsOutByDisciplineId(id: any): Observable<Lector[]> {
    return this.http.get<Lector[]>(`${baseUrl}/lector/withoutDiscipline/${id}`);
  }
}
