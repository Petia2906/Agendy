export interface Ticket {
  id: string;
  eventId: string;
  userId: string;
  ticketType: string;
  price: number;
  status: string;
  purchasedAt: string;
}

export interface CreateTicketRequest {
  ticketType: string;
  price: number;
}
