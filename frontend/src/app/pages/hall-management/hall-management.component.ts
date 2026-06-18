import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HallService } from '../../core/services/hall.service';
import { Hall, CreateHallRequest } from '../../core/models/hall.model';

@Component({
  selector: 'app-hall-management',
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './hall-management.component.html',
  styleUrl: './hall-management.component.scss'
})
export class HallManagementComponent implements OnInit {
  eventId: string = '';
  halls: Hall[] = [];
  loading = true;
  error = '';

  form: CreateHallRequest = {
    name: '',
    capacity: 0
  };

  formError = '';
  showForm = false;

  constructor(
    private route: ActivatedRoute,
    private hallService: HallService
  ) {}

  ngOnInit() {
    this.eventId = this.route.snapshot.paramMap.get('id')!;
    this.loadHalls();
  }

  loadHalls() {
    this.hallService.getHallsByEvent(this.eventId).subscribe({
      next: (data) => {
        this.halls = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load halls.';
        this.loading = false;
      }
    });
  }

  onSubmit() {
    this.hallService.createHall(this.eventId, this.form).subscribe({
      next: (hall) => {
        this.halls.push(hall);
        this.form = { name: '', capacity: 0 };
        this.showForm = false;
        this.formError = '';
      },
      error: () => {
        this.formError = 'Failed to create hall.';
      }
    });
  }

  deleteHall(hallId: string) {
    if (!confirm('Delete this hall?')) return;
    this.hallService.deleteHall(hallId).subscribe({
      next: () => {
        this.halls = this.halls.filter(h => h.id !== hallId);
      },
      error: () => alert('Failed to delete hall.')
    });
  }
}
