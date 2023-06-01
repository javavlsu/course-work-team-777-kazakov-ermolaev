import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Group } from 'src/app/models/group.model';
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
      name: this.group.name
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




}
