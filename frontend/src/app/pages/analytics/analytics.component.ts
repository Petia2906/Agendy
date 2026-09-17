import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AnalyticsService } from '../../core/services/analytics.service';
import { EventAnalyticsResponse } from '../../core/models/analytics.model';
import { HeaderComponent } from '../../shared/header/header.component';

@Component({
  selector: 'app-analytics',
  imports: [RouterLink, CommonModule, HeaderComponent],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent implements OnInit {
  analytics: EventAnalyticsResponse | null = null;
  eventId!: number;
  loading = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private analyticsService: AnalyticsService
  ) {}

  ngOnInit() {
    this.eventId = Number(this.route.snapshot.paramMap.get('id'));

    this.analyticsService.getEventAnalytics(this.eventId).subscribe({
      next: (data) => {
        this.analytics = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.status === 403
          ? 'You are not authorized to view this analytics.'
          : 'Failed to load analytics.';
        this.loading = false;
      }
    });
  }

  getStarArray(rating: number): number[] {
    return Array(rating).fill(0).map((_, i) => i + 1);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
