export interface Ticket {
  id: number;
  eventId: number;
  eventTitle: string;
  userId: number;
  username: string;
  email: string;
  ticketType: string;
  price: number;
  status: string;
  purchasedAt: string;
}

export interface CreateTicketRequest {
  ticketType: string;
}
