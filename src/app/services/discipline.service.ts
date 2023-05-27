import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Discipline } from '../models/discipline.model';

const baseUrl = 'http://localhost:8080/api/discipline';

@Injectable({
  providedIn: 'root'
})
export class DisciplineService {

  constructor( private http: HttpClient) { }

  getAll(): Observable<Discipline[]> {
    return this.http.get<Discipline[]>(baseUrl)
  }

  getLectorsHasDisciplines(idLector: any): Observable<Discipline[]> {
    return this.http.get<Discipline[]>(`${baseUrl}/withLector/${idLector}`)
  }

  getLectorsHasNotDisciplines(idLector: any): Observable<Discipline[]> {
    return this.http.get<Discipline[]>(`${baseUrl}/without/${idLector}`)
  }

  create(data: any): Observable<any> {
    return this.http.post(baseUrl, data)
  }
}
