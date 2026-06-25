export interface Event {
  id: string;
  organizerId: string;
  title: string;
  description: string;
  venue: string;
  eventDate: string;
  capacity: number;
  price: number;
  status: 'DRAFT' | 'PUBLISHED' | 'CANCELLED';
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
