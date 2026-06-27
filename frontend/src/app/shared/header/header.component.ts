import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  protected authService = inject(AuthService);
  private router = inject(Router);

  onLogoClick(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/events']);
    }
    else{
      this.router.navigate(['/login']);
    }
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/']);
  }
}
