export interface Ticket {
  id: number;
  eventId: number;
  eventTitle: string;
  userId: number;
  ticketType: string;
  price: number;
  status: string;
  purchasedAt: string;
}

export interface CreateTicketRequest {
  ticketType: string;
}
