import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventAnalyticsResponse } from '../models/analytics.model';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private apiUrl = 'http://localhost:8080/events';

  constructor(private http: HttpClient) {}

  getEventAnalytics(eventId: number): Observable<EventAnalyticsResponse> {
    return this.http.get<EventAnalyticsResponse>(`${this.apiUrl}/${eventId}/analytics`);
  }
}
