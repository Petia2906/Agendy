import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { LoginRequest } from '../../core/models/user.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  form: LoginRequest = {
    email: '',
    password: ''
  };

  error: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.form).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/events']);
      },
      error: () => {
        this.error = 'Invalid email or password.';
      }
    });
  }
}
