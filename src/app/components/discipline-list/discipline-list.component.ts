import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Discipline } from 'src/app/models/discipline.model';
import { AuthService } from 'src/app/services/auth.service';
import { DisciplineService } from 'src/app/services/discipline.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-discipline-list',
  templateUrl: './discipline-list.component.html',
  styleUrls: ['./discipline-list.component.css']
})
export class DisciplineListComponent {

  private roles: string[] = [];
  disciplines?: Discipline[];
  isLoggedIn = false;
  isAdmin = false;
  isLector = false;

  constructor(
    private disciplieService: DisciplineService,
    private storageService: StorageService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isLector = this.roles.includes('ROLE_LECTOR');
    }

    this.getDiscipline();
  }

  getDiscipline(): void {
    this.disciplieService.getAll()
      .subscribe({
        next: (data) => {
          this.disciplines = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      })
  }
}
