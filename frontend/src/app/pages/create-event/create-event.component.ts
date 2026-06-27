import { Component, OnInit } from '@angular/core';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EventService } from '../../core/services/event.service';
import { CreateEventRequest } from '../../core/models/event.model';

@Component({
  selector: 'app-create-event',
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './create-event.component.html',
  styleUrl: './create-event.component.scss'
})
export class CreateEventComponent implements OnInit {
  form: CreateEventRequest = {
    title: '',
    description: '',
    venue: '',
    eventDate: '',
    capacity: 0,
    price: 0
  };

  error = '';
  editingId: string | null = null;

  constructor(
    private eventService: EventService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.editingId = id;
      this.eventService.getEventById(id).subscribe({
        next: (event) => {
          this.form = {
            title: event.title,
            description: event.description,
            venue: event.venue,
            eventDate: event.eventDate ? event.eventDate.slice(0, 10) : '',
            capacity: event.capacity,
            price: event.price
          };
        },
        error: () => this.error = 'Failed to load event.'
      });
    }
  }

  onSubmit() {
    const payload = {
      ...this.form,
      eventDate: this.form.eventDate + 'T00:00:00'
    };

    if (this.editingId) {
      this.eventService.updateEvent(this.editingId, payload).subscribe({
        next: () => this.router.navigate(['/dashboard']),
        error: () => this.error = 'Failed to update event. Please try again.'
      });
    } else {
      this.eventService.createEvent(payload).subscribe({
        next: () => this.router.navigate(['/dashboard']),
        error: () => this.error = 'Failed to create event. Please try again.'
      });
    }
  }
}
