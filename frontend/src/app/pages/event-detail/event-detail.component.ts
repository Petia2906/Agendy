import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EventService } from '../../core/services/event.service';
import { SessionService } from '../../core/services/session.service';
import { HallService } from '../../core/services/hall.service';
import { TicketService } from '../../core/services/ticket.service';
import { AuthService } from '../../core/services/auth.service';
import { Event } from '../../core/models/event.model';
import { Session } from '../../core/models/session.model';
import { Hall } from '../../core/models/hall.model';

@Component({
  selector: 'app-event-detail',
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './event-detail.component.html',
  styleUrl: './event-detail.component.scss'
})
export class EventDetailComponent implements OnInit {
  event: Event | null = null;
  sessions: Session[] = [];
  halls: Hall[] = [];
  loading = true;
  error = '';

  isAttendee = false;
  isOwner = false;
  private currentUserId: number | null = null;
  selectedTicketType: 'REGULAR' | 'VIP' = 'REGULAR';
  purchasing = false;
  purchaseError = '';
  purchaseSuccess = false;

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private sessionService: SessionService,
    private hallService: HallService,
    private ticketService: TicketService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id')!;

    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.isAttendee = user.role === 'ATTENDEE';
        this.currentUserId = user.id;
        this.updateOwnership();
      },
      error: () => {
        this.isAttendee = false;
        this.currentUserId = null;
        this.updateOwnership();
      }
    });

    this.eventService.getEventById(id).subscribe({
      next: (data) => {
        this.event = data;
        this.updateOwnership();
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

  buyTicket() {
    if (!this.event) return;
    this.purchasing = true;
    this.purchaseError = '';

    this.ticketService.purchaseTicket(this.event.id, { ticketType: this.selectedTicketType }).subscribe({
      next: () => {
        this.purchasing = false;
        this.purchaseSuccess = true;
      },
      error: (err) => {
        this.purchasing = false;
        this.purchaseError = typeof err.error === 'string' ? err.error : 'Failed to purchase ticket.';
      }
    });
  }

  private updateOwnership() {
    this.isOwner = this.event != null && this.currentUserId != null
      && String(this.event.organizerId) === String(this.currentUserId);
  }

  getHallName(hallId: string): string {
    const hall = this.halls.find(h => h.id === hallId);
    return hall ? hall.name : 'Unknown Hall';
  }
}
