import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Discipline } from 'src/app/models/discipline.model';
import { Lector } from 'src/app/models/lector.model';
import { Student } from 'src/app/models/student.model';
import { GroupService } from 'src/app/services/group.service';
import { UserService } from 'src/app/services/user.service';

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

  disciplines: Discipline[] = []
  
  student: Student = {
    name: "",
    email: "",
    groupName: ""
  }

  constructor(private userService: UserService, private groupService: GroupService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    if (this.router.url.includes("students")) {
      this.isLector = false
      this.getStudent(this.route.snapshot.params["idStudent"])
    }
    if (this.router.url.includes("lectors")) {
      this.isLector = true
      this.getLector(this.route.snapshot.params["idLector"])
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


  

}
