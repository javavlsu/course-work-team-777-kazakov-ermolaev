import { DisciplineService } from 'src/app/services/discipline.service';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Discipline } from 'src/app/models/discipline.model';
import { Lector } from 'src/app/models/lector.model';
import { Student } from 'src/app/models/student.model';
import { GroupService } from 'src/app/services/group.service';
import { UserService } from 'src/app/services/user.service';
import { Group } from 'src/app/models/group.model';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent {

  isLector: Boolean = false
  lector: Lector = {
    name: "",
    email: ""
  }

  disciplineWithLector: Discipline[] = [];
  disciplineWithoutLector: Discipline[] = [];

  groups: Group[] = [];

  student: Student = {
    name: "",
    email: "",
    groupName: ""
  }

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    private disciplineServise: DisciplineService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit() {
    if (this.router.url.includes("students")) {
      this.isLector = false;
      this.getStudent(this.route.snapshot.params["idStudent"]);
    }
    if (this.router.url.includes("lectors")) {
      this.isLector = true;
      this.getLector(this.route.snapshot.params["idLector"]);
      this.getDisciplines(this.route.snapshot.params["idLector"]);
    }
  }

  getStudent(id: any) {
    this.userService.getStudentById(id)
    .subscribe({
      next: (data) => {
        this.student = data;
      },
      error: (e) => console.error(e)
    })
  }

  getLector(id: any) {
    this.userService.getLectorById(id)
    .subscribe({
      next: (data) => {
        this.lector = data;
      },
      error: (e) => console.error(e)
    })
  }

  getDisciplines(idLector: any) {
    this.disciplineServise.getLectorsHasDisciplines(idLector)
      .subscribe({
        next: (data) => {
          this.disciplineWithLector = data;
        },
        error: (e) => console.error(e)
      })

    this.disciplineServise.getLectorsHasNotDisciplines(idLector)
      .subscribe({
        next: (data) => {
          this.disciplineWithoutLector = data;
        },
        error: (e) => console.error(e)
      })
  }

  addDisciplene(discipline: Discipline) {
    this.disciplineWithLector.forEach( (item, index) => {
      if(item === discipline) this.disciplineWithLector.splice(index, 1);
    });

    this.disciplineWithoutLector.push(discipline);
  }

  removeDisciplene(discipline: Discipline) {
    this.disciplineWithLector.push(discipline);

    this.disciplineWithoutLector.forEach( (item, index) => {
      if(item === discipline) this.disciplineWithoutLector.splice(index, 1);
    });
  }

  getGroup() {
    this.groupService.getAll()
      .subscribe({
        next: (data) => {
          this.groups = data;
        }
      })
  }

  updateLector() {
    const idLector = this.route.snapshot.params["idLector"];

    const data = {
      name: this.lector.name,
      email: this.lector.email,
      disciplineList: this.disciplineWithLector
    }

    this.userService.updateLector(idLector, data)
      .subscribe({
        next: () => {},
        error: (e) => console.error(e)
      })

    this.router.navigate([`/users`])
  }

  updateStudent() {
    const idStudent = this.route.snapshot.params["idStudent"];


  }
}
