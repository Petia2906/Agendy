export interface Session {
  id: number;
  speakerId: number | null;
  speakerName?: string;
  hallId: number;
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
  hallId: number | null;
  speakerId: number | null;
}

export interface UpdateSessionRequest {
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  hallId: number | null;
  speakerId: number | null;
}
