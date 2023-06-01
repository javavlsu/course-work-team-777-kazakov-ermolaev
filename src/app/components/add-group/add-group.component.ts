import { Component } from '@angular/core';
import { Group } from 'src/app/models/group.model';
import { GroupService } from 'src/app/services/group.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrls: ['./add-group.component.css']
})
export class AddGroupComponent {
  private roles: string[] = [];
  submitted = false;
  isLoggedIn = false;
  isAdmin = false;

  group: Group = {
    name: ""
  }

  constructor(
    private groupService: GroupService,
    private userService: UserService,
    private storageService: StorageService
  ) {

  }

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
    }
  }

  newGroup() {
    this.group = {
      name: ''
    }
    this.submitted = false;
  }

  saveGroup() {
    const data = {
      name: this.group.name
    }

    this.groupService.createGroup(data)
      .subscribe({
        next: (res) => {
          console.log(res)
          this.submitted = true;
        },
        error: (e) => console.error(e)
      })
  }

}
