import { Lector } from './../../models/lector.model';
import { DisciplineService } from './../../services/discipline.service';
import { Discipline } from './../../models/discipline.model';
import { Component } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GroupService } from 'src/app/services/group.service';
import { Group } from 'src/app/models/group.model';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-discipline-details',
  templateUrl: './discipline-details.component.html',
  styleUrls: ['./discipline-details.component.css']
})
export class DisciplineDetailsComponent {

  private roles: string[] = [];
  isLoggedIn = false;
  isAdmin = false;
  isLector = false;

  discipline: Discipline = {
    title: ""
  }

  lectorInDiscipline: Lector[] = [];
  lectorOutDiscipline: Lector[] = [];
  groupInDiscipline: Group[] = [];
  groupOutDiscipline: Group[] = [];

  constructor(
    private disciplineService: DisciplineService,
    private userService: UserService,
    private groupService: GroupService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();
    let idDiscipline = this.route.snapshot.params["idDiscipline"];

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isLector = this.roles.includes('ROLE_LECTOR');
    }

    this.getDiscipline(idDiscipline);
    this.getLectors(idDiscipline);
    this.getGroups(idDiscipline);
  }

  getDiscipline(id: any) {
    this.disciplineService.getById(id)
      .subscribe({
        next: (data) => {
          this.discipline = data;
        },
        error: (e) => console.error(e)
      })
  }

  getLectors(id: any) {
    this.disciplineService.getLectorsInByDisciplineId(id)
      .subscribe({
        next: (data) => {
          this.lectorInDiscipline = data;
        },
        error: (e) => console.error(e)
      })

    this.disciplineService.getLectorsOutByDisciplineId(id)
      .subscribe({
        next: (data) => {
          this.lectorOutDiscipline = data;
        },
        error: (e) => console.error(e)
      })
  }

  getGroups(id: any) {
    this.disciplineService.getGroupInByDisciplineId(id)
      .subscribe({
        next: (data) => {
          this.groupInDiscipline = data;
        },
        error: (e) => console.error(e)
      })

    this.disciplineService.getGroupOutByDisciplineId(id)
      .subscribe({
        next: (data) => {
          this.groupOutDiscipline = data;
        },
        error: (e) => console.error(e)
      })
  }

  addlector(lector: Lector) {
    this.lectorInDiscipline.push(lector);

    this.lectorOutDiscipline.forEach( (item, index) => {
      if(item === lector) this.lectorOutDiscipline.splice(index, 1);
    });
  }

  removeLector(lector: Lector) {
    this.lectorInDiscipline.forEach( (item, index) => {
      if(item === lector) this.lectorInDiscipline.splice(index, 1);
    });

    this.lectorOutDiscipline.push(lector);
  }

  addGroup(group: Group) {
    this.groupInDiscipline.push(group);

    this.groupOutDiscipline.forEach( (item, index) => {
      if(item === group) this.groupOutDiscipline.splice(index, 1);
    });
  }

  removeGroup(group: Group) {
    this.groupInDiscipline.forEach( (item, index) => {
      if(item === group) this.groupInDiscipline.splice(index, 1);
    });

    this.groupOutDiscipline.push(group);
  }

  saveDiscipline() {
    const idDiscipline = this.route.snapshot.params["idDiscipline"]

    const data = {
      title: this.discipline.title,
      lectorResponseList: this.lectorInDiscipline,
      groupList: this.groupInDiscipline
    }

    this.disciplineService.update(idDiscipline, data)
      .subscribe({
        next: (res) => {
          console.log(res);
        },
        error: (e) => console.error(e)
      });

    this.router.navigate([`/discipline`])
  }


}
