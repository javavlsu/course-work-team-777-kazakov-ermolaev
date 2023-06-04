import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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

  deleteDiscipline(id: any) {
    this.disciplieService.delete(id)
      .subscribe({
        next: () => {
          this.getDiscipline();
        },
        error: (e) => console.error(e)
      })
  }

  redirectToDisciplineDetails(id: any) {
    this.router.navigate([`/discipline/${id}`])
  }

  redirectToDisciplinePage(id: any) {
    this.router.navigate([`/discipline/${id}/page`])
  }
}
