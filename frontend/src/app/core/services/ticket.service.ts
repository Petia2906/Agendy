import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ticket, CreateTicketRequest } from '../models/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  purchaseTicket(eventId: number, request: CreateTicketRequest): Observable<Ticket> {
    return this.http.post<Ticket>(`${this.apiUrl}/events/${eventId}/tickets`, request);
  }

  getMyTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${this.apiUrl}/tickets/my`);
  }

  cancelTicket(ticketId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/tickets/${ticketId}/cancel`, {});
  }
}
