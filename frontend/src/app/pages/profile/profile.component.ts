import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../shared/header/header.component';
import { AuthService, CurrentUser } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, RouterLink, HeaderComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  user: CurrentUser | null = null;
  loading = true;
  error = '';

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.getCurrentUser().subscribe({
      next: (data) => {
        this.user = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load profile.';
        this.loading = false;
      }
    });
  }
}
