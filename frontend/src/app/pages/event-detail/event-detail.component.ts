import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { EventService } from '../../core/services/event.service';
import { SessionService } from '../../core/services/session.service';
import { HallService } from '../../core/services/hall.service';
import { Event } from '../../core/models/event.model';
import { Session } from '../../core/models/session.model';
import { Hall } from '../../core/models/hall.model';

@Component({
  selector: 'app-event-detail',
  imports: [RouterLink, CommonModule],
  templateUrl: './event-detail.component.html',
  styleUrl: './event-detail.component.scss'
})
export class EventDetailComponent implements OnInit {
  event: Event | null = null;
  sessions: Session[] = [];
  halls: Hall[] = [];
  loading = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private sessionService: SessionService,
    private hallService: HallService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id')!;

    this.eventService.getEventById(id).subscribe({
      next: (data) => {
        this.event = data;
        this.loading = false;

        this.sessionService.getSessionsByEvent(id).subscribe({
          next: (s) => this.sessions = s,
          error: () => {}
        });

        this.hallService.getHallsByEvent(id).subscribe({
          next: (h) => this.halls = h,
          error: () => {}
        });
      },
      error: () => {
        this.error = 'Event not found.';
        this.loading = false;
      }
    });
  }

  getHallName(hallId: string): string {
    const hall = this.halls.find(h => h.id === hallId);
    return hall ? hall.name : 'Unknown Hall';
  }
}
