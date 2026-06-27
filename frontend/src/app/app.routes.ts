import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { noAuthGuard } from './core/guards/no-auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { attendeeGuard } from './core/guards/attendee.guard';

export const routes: Routes = [
  {
    path: '',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
  },
  {
    path: 'login',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'events/:id/sessions',
    canActivate: [adminGuard],
    loadComponent: () => import('./pages/session-management/session-management.component').then(m => m.SessionManagementComponent)
  },
  {
    path: 'events/:id/halls',
    canActivate: [adminGuard],
    loadComponent: () => import('./pages/hall-management/hall-management.component').then(m => m.HallManagementComponent)
  },
  {
    path: 'events/:id/edit',
    canActivate: [adminGuard],
    loadComponent: () => import('./pages/create-event/create-event.component').then(m => m.CreateEventComponent)
  },
  {
    path: 'dashboard/create',
    canActivate: [adminGuard],
    loadComponent: () => import('./pages/create-event/create-event.component').then(m => m.CreateEventComponent)
  },
  {
    path: 'events',
    canActivate: [attendeeGuard],
    loadComponent: () => import('./pages/event-list/event-list.component').then(m => m.EventListComponent)
  },
  {
    path: 'events/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/event-detail/event-detail.component').then(m => m.EventDetailComponent)
  },
  {
    path: 'events/:id/analytics',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/analytics/analytics.component').then(m => m.AnalyticsComponent)
  },
  {
    path: 'my-tickets',
    canActivate: [attendeeGuard],
    loadComponent: () => import('./pages/my-tickets/my-tickets.component').then(m => m.MyTicketsComponent)
  },
  {
    path: 'dashboard',
    canActivate: [adminGuard],
    loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent)
  },
  {
    path: 'admin/speakers',
    canActivate: [adminGuard],
    loadComponent: () => import('./pages/speaker-management/speaker-management.component').then(m => m.SpeakerManagementComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
