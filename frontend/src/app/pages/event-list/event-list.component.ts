import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { EventService } from '../../core/services/event.service';
import { Event } from '../../core/models/event.model';
import { HeaderComponent } from '../../shared/header/header.component';

type StatusFilter = 'ALL' | 'UPCOMING' | 'TODAY' | 'PAST';

@Component({
  selector: 'app-event-list',
  imports: [RouterLink, CommonModule, HeaderComponent],
  templateUrl: './event-list.component.html',
  styleUrl: './event-list.component.scss'
})
export class EventListComponent implements OnInit {
  events: Event[] = [];
  loading = true;
  error = '';

  activeFilter: StatusFilter = 'ALL';
  readonly filters: StatusFilter[] = ['ALL', 'UPCOMING', 'TODAY', 'PAST'];

  constructor(private eventService: EventService) {}

  get filteredEvents(): Event[] {
    if (this.activeFilter === 'ALL') return this.events;
    return this.events.filter(e => e.status === this.activeFilter);
  }

  setFilter(filter: StatusFilter): void {
    this.activeFilter = filter;
  }

  ngOnInit() {
    this.eventService.getAllEvents().subscribe({
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
}
