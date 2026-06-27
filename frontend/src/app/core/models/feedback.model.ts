export interface Feedback {
  id: number;
  eventId: number;
  userId: number;
  userName: string;
  rating: number;
  comment: string;
  createdAt: string;
}

export interface CreateFeedbackRequest {
  rating: number;
  comment: string;
}