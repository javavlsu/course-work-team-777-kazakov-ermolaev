import { Student } from './../../models/student.model';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DisciplineService } from 'src/app/services/discipline.service';

@Component({
  selector: 'app-discipline-result-page',
  templateUrl: './discipline-result-page.component.html',
  styleUrls: ['./discipline-result-page.component.css']
})
export class DisciplineResultPageComponent {
  students: Student[] = [];
  private idDiscipline = this.route.snapshot.params["idDiscipline"];

  constructor(
    private disciplineService: DisciplineService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit() {
    this.getStudent();
  }

  getStudent() {
    this.disciplineService.getScore(this.idDiscipline)
      .subscribe({
        next: (data) => this.students = data,
        error: (e) => console.error(e)
      })
  }

  redirectToDisciplinePage() {
    this.router.navigate([`/discipline/${this.idDiscipline}/page`])
  }
}
