import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Hall, CreateHallRequest } from '../models/hall.model';

@Injectable({
  providedIn: 'root'
})
export class HallService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getHallsByEvent(eventId: string): Observable<Hall[]> {
    return this.http.get<Hall[]>(`${this.apiUrl}/events/${eventId}/halls`);
  }

  createHall(eventId: string, request: CreateHallRequest): Observable<Hall> {
    return this.http.post<Hall>(`${this.apiUrl}/events/${eventId}/halls`, request);
  }

  deleteHall(hallId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/halls/${hallId}`);
  }
}
