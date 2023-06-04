import { TestService } from 'src/app/services/test.service';
import { Component } from '@angular/core';
import { Test } from 'src/app/models/test.model';
import { StorageService } from 'src/app/services/storage.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-test-page',
  templateUrl: './test-page.component.html',
  styleUrls: ['./test-page.component.css']
})
export class TestPageComponent {
  private idTest = this.route.snapshot.params["idTest"];
  private idDiscipline = this.route.snapshot.params["idDiscipline"];

  test: Test = {
    title: "",
    dateStart: new Date(),
    deadline: new Date(),
    status: ""
  }

  isOpen = false;

  constructor(
    private testService: TestService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit() {
    this.getTest();
  }

  getTest() {
    this.testService.getTestById(this.idDiscipline, this.idTest)
      .subscribe({
        next: (data) => {
          this.test = data;
          if (data.status?.includes("open")) {
            this.isOpen = true;
          }
          if (data.status?.includes("close")) {
            this.isOpen = false;
          }
        },
        error: (e) => console.error(e)
      });
  }

  redirectToDiscipline() {
    this.router.navigate([`/discipline/${this.idDiscipline}/page`])
  }
}
