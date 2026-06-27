export interface Event {
  id: number;
  organizerId: number;
  title: string;
  description: string;
  venue: string;
  eventDate: string;
  capacity: number;
  price: number;
  status: 'PAST' | 'TODAY' | 'UPCOMING';
  createdAt: string;
}

export interface CreateEventRequest {
  title: string;
  description: string;
  venue: string;
  eventDate: string;
  capacity: number;
  price: number;
}

export interface UpdateEventRequest {
  title?: string;
  description?: string;
  venue?: string;
  eventDate?: string;
  capacity?: number;
  price?: number;
}
