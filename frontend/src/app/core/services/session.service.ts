import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Session, CreateSessionRequest, UpdateSessionRequest } from '../models/session.model';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getSessionsByEvent(eventId: string): Observable<Session[]> {
    return this.http.get<Session[]>(`${this.apiUrl}/events/${eventId}/sessions`);
  }

  createSession(eventId: string, request: CreateSessionRequest): Observable<Session> {
    return this.http.post<Session>(`${this.apiUrl}/events/${eventId}/sessions`, request);
  }

  updateSession(sessionId: string, request: UpdateSessionRequest): Observable<Session> {
    return this.http.put<Session>(`${this.apiUrl}/sessions/${sessionId}`, request);
  }

  deleteSession(sessionId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/sessions/${sessionId}`);
  }
}
