import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Lector } from 'src/app/models/lector.model';
import { Student } from 'src/app/models/student.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent {
  students: Student[] = [];
  lectors: Lector[] = [];
  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {
    this.getLector();
    this.getStudent();
  }
  getStudent() {
    this.userService.getStudent()
      .subscribe({
        next: (data) => {
          this.students = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
  getLector() {
    this.userService.getLector()
      .subscribe({
        next: (data) => {
          this.lectors = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  deleteStudent(id: any) {
    this.userService.deleteStudent(id)
     .subscribe({
      next: () => {
        this.getStudent();
      },
      error: (e) => console.error(e)
     });
  }

  deleteLector(id: any) {
    this.userService.deleteLector(id)
     .subscribe({
      next: () => {
        this.getLector();
      },
      error: (e) => console.error(e)
     });
  }

  redirectToStudentDetails(id: any) {
    this.router.navigate([`/users/students/${id}`]);
  }

  redirectToLectorDetails(id: any) {
    this.router.navigate([`/users/lectors/${id}`]);
  }

  newuser() {
    this.router.navigate([`newuser`]);
  }
  
}
