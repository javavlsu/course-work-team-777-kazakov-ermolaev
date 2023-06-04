import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Group } from 'src/app/models/group.model';
import { GroupService } from 'src/app/services/group.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.css']
})
export class GroupListComponent {
  private roles: string[] = [];
  groups?: Group[];
  isLoggedIn = false;
  isAdmin = false;
  isLector = false;

  create = false;
  isCreateFailed = false;
  errorMessage = '';

  group: Group = {
    name: ""
  }

  constructor(
    private groupService: GroupService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isLector = this.roles.includes('ROLE_LECTOR');
    }

    this.getGroups();
  }

  getGroups(): void {
    this.groupService.getAll()
      .subscribe({
        next: (data) => {
          this.groups = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      })
  }

  openCreateForm() {
    this.create = true;
  }

  closeCreateForm() {
    this.create = false;
  }

  saveGroup() {
    const data = {
      name: this.group.name
    }

    this.groupService.createGroup(data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.isCreateFailed = false;
          this.group = {
            name: ''
          }
          this.getGroups();
        },
        error: (e) => {
          this.errorMessage = e.error.message;
          this.isCreateFailed = true;
        }
      })


  }

  deleteGroup(id: any) {
    this.groupService.deleteGroup(id)
      .subscribe({
        next: () => {
          this.getGroups();
        },
        error: (e) => console.error(e)
      })
  }
  redirectToGroupDetails(id: any) {
    this.router.navigate([`/groups/${id}`])
  }


}
