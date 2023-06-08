import { LabworkService } from 'src/app/services/labwork.service';
import { LabWork } from 'src/app/models/labwork.model';
import { Component } from '@angular/core';
import { StorageService } from 'src/app/services/storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FileModel } from 'src/app/models/file.model';
import { StudentLabWork } from 'src/app/models/studentlabwork.model';
import { Student } from 'src/app/models/student.model';

@Component({
  selector: 'app-labwork-page',
  templateUrl: './labwork-page.component.html',
  styleUrls: ['./labwork-page.component.css']
})
export class LabworkPageComponent {
  private idLabWork = this.route.snapshot.params["idLabWork"];
  private idDiscipline = this.route.snapshot.params["idDiscipline"];


  private roles: string[] = [];
  isLoggedIn = false;
  isAdmin = false;
  isLector = false;
  isStudent = false;


  files: FileModel = {
    name: "",
    path: ""
  }

  studentFiles = false;

  labWork: LabWork = {
    title: "",
    manual: "",
    deadline: new Date()
  }

  scores: Student[] = [];

  fileToUpload: File | null = null;

  constructor(
    private labworkService: LabworkService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;
      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isLector = this.roles.includes('ROLE_LECTOR');
      this.isStudent = !this.roles.includes('ROLE_LECTOR');
    }

    this.getLabWork();
    this.getScores();
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

  redirectToDiscipline() {
    this.router.navigate([`/discipline/${this.idDiscipline}/page`])
  }

  openStudentFiles() {
    this.studentFiles = true;
  }

  closeStudentFiles() {
    this.studentFiles = false;
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }

  getScores() {
    this.labworkService.getScore(this.idDiscipline, this.idLabWork)
    .subscribe({
      next: (data) => {
        this.scores = data;
      },
      error: (e) => console.error(e)
    });
  }

  createScoreLabWork() {
    this.labworkService.createScoreLabWork(this.idDiscipline, this.idLabWork)
    .subscribe({
      next: (data) => console.log(data),
      error: (e) => console.error(e)
    });
  }

  updateScoreLabWork(scoreLab: any, idStudent: any) {
    const data = {
      idStudent: idStudent,
      score: scoreLab
    }
    this.labworkService.updateScoreLabWork(this.idLabWork, this.idDiscipline, data)
      .subscribe({
        next: (data) => console.log(data),
        error: (e) => console.error(e)
      });
  }

}
