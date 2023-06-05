import { LabworkService } from './../../services/labwork.service';
import { LabWork } from 'src/app/models/labwork.model';
import { Component } from '@angular/core';
import { StorageService } from 'src/app/services/storage.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-labwork-edit',
  templateUrl: './labwork-edit.component.html',
  styleUrls: ['./labwork-edit.component.css']
})
export class LabworkEditComponent {
  private idLabWork = this.route.snapshot.params["idLabWork"];
  private idDiscipline = this.route.snapshot.params["idDiscipline"];

  labWork: LabWork = {
    title: "",
    manual: "",
    deadline: new Date()
  }

  isUpdateFailed = false;
  errorMessage = "";

  constructor(
    private labworkService: LabworkService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

    ngOnInit() {
      this.getLabWork();
    }

    getLabWork() {
      this.labworkService.getLabWorkById(this.idDiscipline, this.idLabWork)
        .subscribe({
          next: (data) => {
            this.labWork = data;
          },
          error: (e) => console.error(e)
        });
    }

    saveLabWork() {
      const data = {
        title: this.labWork.title,
        manual: this.labWork.manual,
        deadline: this.labWork.deadline
      }

      this.labworkService.updateLabWork(this.idDiscipline, this.idLabWork, data)
        .subscribe({
          next: (res) => {
            console.log(res);
            this.isUpdateFailed = false;
            this.redirectToDiscipline();

          },
          error: (e) => {
            this.errorMessage = e.error.message;
            this.isUpdateFailed = true;
          }
        })
    }

    redirectToDiscipline() {
      this.router.navigate([`/discipline/${this.idDiscipline}/page`])
    }

}
