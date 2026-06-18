export interface User {
  id: string;
  name: string;
  email: string;
  role: 'ATTENDEE' | 'ADMIN' | 'SPEAKER';
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  role: 'ATTENDEE' | 'ADMIN' | 'SPEAKER';
}

export interface AuthResponse {
  token: string;
  user: User;
}
