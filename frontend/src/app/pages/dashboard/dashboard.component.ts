import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { EventService } from '../../core/services/event.service';
import { Event } from '../../core/models/event.model';
import { HeaderComponent } from '../../shared/header/header.component';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, CommonModule, HeaderComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  events: Event[] = [];
  loading = true;
  error = '';

  constructor(private eventService: EventService) {}

  ngOnInit() {
    this.eventService.getMyEvents().subscribe({
      next: (data) => {
        this.events = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load events.';
        this.loading = false;
      }
    });
  }

  deleteEvent(id: number) {
    if (!confirm('Are you sure you want to delete this event?')) return;

    this.eventService.deleteEvent(id).subscribe({
      next: () => {
        this.events = this.events.filter(e => e.id !== id);
      },
      error: () => {
        alert('Failed to delete event.');
      }
    });
  }
}
