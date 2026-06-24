export interface FeedbackResponse {
  id: number;
  eventId: number;
  userId: number;
  userName: string;
  rating: number;
  comment: string;
  createdAt: string;
}

export interface TicketResponse {
  id: number;
  eventId: number;
  eventTitle: string;
  userId: number;
  ticketType: string;
  price: number;
  status: string;
  purchasedAt: string;
}

export interface EventAnalyticsResponse {
  ratings: FeedbackResponse[];
  ticketBuyers: TicketResponse[];
}
