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
