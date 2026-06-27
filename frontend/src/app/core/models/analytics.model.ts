import { Feedback } from './feedback.model';
import { Ticket } from './ticket.model';

export interface EventAnalyticsResponse {
  ratings: Feedback[];
  ticketBuyers: Ticket[];
}
