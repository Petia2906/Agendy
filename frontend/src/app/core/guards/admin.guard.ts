import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    return router.createUrlTree(['/login']);
  }

  return authService.getCurrentUser().pipe(
    map(user => user.role === 'ADMIN' ? true : router.createUrlTree(['/events'])),
    catchError(() => of(router.createUrlTree(['/login'])))
  );
};