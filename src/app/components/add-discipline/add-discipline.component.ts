import { DisciplineService } from 'src/app/services/discipline.service';
import { Discipline } from 'src/app/models/discipline.model';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Lector } from 'src/app/models/lector.model';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-add-discipline',
  templateUrl: './add-discipline.component.html',
  styleUrls: ['./add-discipline.component.css']
})
export class AddDisciplineComponent {

  form: FormGroup;

  private roles: string[] = [];
  submitted = false;
  isLoggedIn = false;
  isAdmin = false;

  isCreateFailed = false;
  errorMessage = '';

  lectors: Lector[] = [];
  selectedLector: Lector[] = [];

  discipline: Discipline = {
    title: ""
  }


  constructor(
    private disciplineService: DisciplineService,
    private userService: UserService,
    private storageService: StorageService,
    fb: FormBuilder) {
      this.form = fb.group({
        pickedUsers:  new FormArray([])
       });
    }

  ngOnInit() {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
    }

    this.getLectors();
  }

  getLectors() {
    this.userService.getLector()
      .subscribe({
        next: (data) => {
          this.lectors = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      })
  }

  saveDiscipline() {
    const data = {
      title: this.discipline.title,
      lectorResponseList: this.selectedLector
    }

    this.disciplineService.create(data)
      .subscribe({
        next: (res) => {
          console.log(res)
          this.submitted = true;
        },
        error: (e) => {
          this.errorMessage = e.error.message;
          this.submitted = false;
          this.isCreateFailed = true;
        }
      })
  }

  newDiscipline() {
    this.discipline = {
      title: ''
    }

    this.lectors = [];
    this.submitted = false;
  }

  onCheckboxChange(event: any, lector: Lector) {

    const pickedUsers = (this.form.controls['pickedUsers'] as FormArray);
    if (event.target.checked) {
      pickedUsers.push(new FormControl(event.target.value));
      this.selectedLector.push(lector);
    } else {
      const index = pickedUsers.controls
      .findIndex(x => x.value == event.target.value);
      pickedUsers.removeAt(index);
      this.selectedLector.forEach( (item, index) => {
        if(item == lector) this.selectedLector.splice(index, 1);
      });
    }
  }
}
