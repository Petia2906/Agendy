import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from '../models/event.model';

@Injectable({
  providedIn: 'root'
})
export class SpeakerSelfService {
  private apiUrl = 'http://localhost:8080/speakers';

  constructor(private http: HttpClient) {}

  getMyEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/me/events`);
  }
}
