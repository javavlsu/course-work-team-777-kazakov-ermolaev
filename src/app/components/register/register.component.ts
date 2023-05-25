import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  roles = [ "student", "lector" , "admin" ];

  form: any = {
    username: null,
    email: null,
    password: null,
    fullName: null,
    birthdate: null,
    role: []
  };

  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const { username, email, password, fullName, birthdate, role } = this.form;

    this.authService.register(username, email, password, fullName, birthdate, role).subscribe({
      next: data => {
        console.log(data);
        this.isSignUpFailed = false;
        this.router.navigate([`users`]);
      },
      error: (err) => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    });
  }
}
