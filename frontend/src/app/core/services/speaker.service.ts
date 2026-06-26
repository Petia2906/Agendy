import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Speaker, CreateSpeakerRequest } from '../models/speaker.model';

@Injectable({
  providedIn: 'root'
})
export class SpeakerService {
  private apiUrl = 'http://localhost:8080/admin/speakers';

  constructor(private http: HttpClient) {}

  createSpeaker(request: CreateSpeakerRequest): Observable<Speaker> {
    return this.http.post<Speaker>(this.apiUrl, request);
  }

  getAllSpeakers(): Observable<Speaker[]> {
    return this.http.get<Speaker[]>(this.apiUrl);
  }
}
