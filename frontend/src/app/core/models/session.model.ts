export interface Session {
  id: string;
  eventId: string;
  speakerId: string;
  hallId: string;
  title: string;
  description: string;
  startTime: string;
  endTime: string;
}

export interface CreateSessionRequest {
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  hallId: string;
  speakerId: string;
}

export interface UpdateSessionRequest {
  title?: string;
  description?: string;
  startTime?: string;
  endTime?: string;
  hallId?: string;
}
