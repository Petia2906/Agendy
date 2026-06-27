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
  eventId!: number;
  halls: Hall[] = [];
  loading = true;
  error = '';

  form: CreateHallRequest = {
    name: '',
    capacity: 0
  };

  formError = '';
  showForm = false;
  editingId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private hallService: HallService
  ) {}

  ngOnInit() {
    this.eventId = Number(this.route.snapshot.paramMap.get('id'));
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

  openCreateForm() {
    this.editingId = null;
    this.form = { name: '', capacity: 0 };
    this.formError = '';
    this.showForm = true;
  }

  editHall(hall: Hall) {
    this.editingId = hall.id;
    this.form = { name: hall.name, capacity: hall.capacity };
    this.formError = '';
    this.showForm = true;
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.form = { name: '', capacity: 0 };
    this.formError = '';
  }

  onSubmit() {
    if (this.editingId) {
      this.hallService.updateHall(this.eventId, this.editingId, this.form).subscribe({
        next: (updated) => {
          this.halls = this.halls.map(h => h.id === this.editingId ? updated : h);
          this.cancelForm();
        },
        error: () => this.formError = 'Failed to update hall.'
      });
    } else {
      this.hallService.createHall(this.eventId, this.form).subscribe({
        next: (hall) => {
          this.halls.push(hall);
          this.cancelForm();
        },
        error: () => this.formError = 'Failed to create hall.'
      });
    }
  }

  deleteHall(hallId: number) {
    if (!confirm('Delete this hall?')) return;
    this.hallService.deleteHall(this.eventId, hallId).subscribe({
      next: () => {
        this.halls = this.halls.filter(h => h.id !== hallId);
      },
      error: () => alert('Failed to delete hall.')
    });
  }
}
