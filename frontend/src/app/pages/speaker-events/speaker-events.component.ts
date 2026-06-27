import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SpeakerSelfService } from '../../core/services/speaker-self.service';
import { Event } from '../../core/models/event.model';
import { HeaderComponent } from '../../shared/header/header.component';

@Component({
  selector: 'app-speaker-events',
  imports: [CommonModule, RouterLink, HeaderComponent],
  templateUrl: './speaker-events.component.html',
  styleUrl: './speaker-events.component.scss'
})
export class SpeakerEventsComponent implements OnInit {
  events: Event[] = [];
  loading = true;
  error = '';

  constructor(private speakerSelfService: SpeakerSelfService) {}

  ngOnInit() {
    this.speakerSelfService.getMyEvents().subscribe({
      next: (data) => {
        this.events = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load your events.';
        this.loading = false;
      }
    });
  }
}
