import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Feedback, CreateFeedbackRequest } from '../models/feedback.model';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private apiUrl = 'http://localhost:8080/events';

  constructor(private http: HttpClient) {}

  getEventFeedback(eventId: number): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/${eventId}/feedback`);
  }

  createFeedback(eventId: number, request: CreateFeedbackRequest): Observable<Feedback> {
    return this.http.post<Feedback>(`${this.apiUrl}/${eventId}/feedback`, request);
  }
}