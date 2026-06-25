import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
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
export class CreateEventComponent {
  form: CreateEventRequest = {
    title: '',
    description: '',
    venue: '',
    eventDate: '',
    capacity: 0,
    price: 0
  };

  error = '';

  constructor(private eventService: EventService, private router: Router) {}

  onSubmit() {
    const payload = {
      ...this.form,
      eventDate: this.form.eventDate + 'T00:00:00'
    };

    this.eventService.createEvent(payload).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.error = 'Failed to create event. Please try again.';
      }
    });
  }
}
