import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Group } from 'src/app/models/group.model';
import { Student } from 'src/app/models/student.model';
import { GroupService } from 'src/app/services/group.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-group-details',
  templateUrl: './group-details.component.html',
  styleUrls: ['./group-details.component.css']
})
export class GroupDetailsComponent {
  private roles: string[] = [];
  isLoggedIn = false;
  isAdmin = false;
  isLector = false;

  group: Group = {
    name: ""
  }

  studentsInGroup: Student[] = [];
  studentsOutGroup: Student[] = [];

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();
    let idGroup = this.route.snapshot.params["idGroup"]

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isLector = this.roles.includes('ROLE_LECTOR');
    }

    this.getGroup(idGroup);

    this.getStudents(idGroup);
  }

  getGroup(id: any) {
    this.groupService.getById(id)
      .subscribe({
        next: (data) => {
          this.group = data;
        },
        error: (e) => console.error(e)
      })
  }

  saveGroup() {
    const idGroup = this.route.snapshot.params["idGroup"]

    const data = {
      name: this.group.name,
      studentResponseList: this.studentsInGroup
    }

    this.groupService.updateGroup(idGroup, data)
      .subscribe({
        next: (res) => {
          console.log(res);
        },
        error: (e) => console.error(e)
      });

    this.router.navigate([`/groups`])
  }

  addStudent(student: Student) {
    this.studentsInGroup.push(student);

    this.studentsOutGroup.forEach( (item, index) => {
      if(item === student) this.studentsOutGroup.splice(index, 1);
    });
  }

  removeStudent(student: Student) {
    this.studentsInGroup.forEach( (item, index) => {
      if(item === student) this.studentsInGroup.splice(index, 1);
    });

    this.studentsOutGroup.push(student);
  }

  getStudents(idGroup: any) {
    this.groupService.getStudentsWithGroup(idGroup)
      .subscribe({
        next: (data) => {
          this.studentsInGroup = data;
        },
        error: (e) => console.error(e)
      })
      this.groupService.getStudentsWithoutGroup()
        .subscribe({
          next: (data) => {
            this.studentsOutGroup = data;
          },
          error: (e) => console.error(e)
        })
  }
  




}
