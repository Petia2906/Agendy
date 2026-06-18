import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'events/:id/sessions',
    loadComponent: () => import('./pages/session-management/session-management.component').then(m => m.SessionManagementComponent)
  },
  {
    path: 'events/:id/halls',
    loadComponent: () => import('./pages/hall-management/hall-management.component').then(m => m.HallManagementComponent)
  },
  {
    path: 'dashboard/create',
    loadComponent: () => import('./pages/create-event/create-event.component').then(m => m.CreateEventComponent)
  },
  {
    path: 'events',
    loadComponent: () => import('./pages/event-list/event-list.component').then(m => m.EventListComponent)
  },
  {
    path: 'events/:id',
    loadComponent: () => import('./pages/event-detail/event-detail.component').then(m => m.EventDetailComponent)
  },
  {
    path: 'my-tickets',
    loadComponent: () => import('./pages/my-tickets/my-tickets.component').then(m => m.MyTicketsComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
