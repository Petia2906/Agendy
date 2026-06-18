import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { RegisterRequest } from '../../core/models/user.model';

@Component({
  selector: 'app-register',
  imports: [RouterLink, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  form: RegisterRequest = {
    name: '',
    email: '',
    password: '',
    role: 'ATTENDEE'
  };

  error: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.register(this.form).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/events']);
      },
      error: () => {
        this.error = 'Registration failed. Please try again.';
      }
    });
  }
}
