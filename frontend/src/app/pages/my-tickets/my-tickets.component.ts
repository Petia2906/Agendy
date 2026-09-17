import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TicketService } from '../../core/services/ticket.service';
import { Ticket } from '../../core/models/ticket.model';
import { HeaderComponent } from '../../shared/header/header.component';

@Component({
  selector: 'app-my-tickets',
  imports: [RouterLink, CommonModule, HeaderComponent],
  templateUrl: './my-tickets.component.html',
  styleUrl: './my-tickets.component.scss'
})
export class MyTicketsComponent implements OnInit {
  tickets: Ticket[] = [];
  loading = true;
  error = '';

  constructor(private ticketService: TicketService) {}

  ngOnInit() {
    this.ticketService.getMyTickets().subscribe({
      next: (data) => {
        this.tickets = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load tickets.';
        this.loading = false;
      }
    });
  }

  cancelTicket(ticketId: number) {
    if (!confirm('Cancel this ticket?')) return;
    this.ticketService.cancelTicket(ticketId).subscribe({
      next: () => {
        const ticket = this.tickets.find(t => t.id === ticketId);
        if (ticket) ticket.status = 'CANCELLED';
      },
      error: () => alert('Failed to cancel ticket.')
    });
  }
}
