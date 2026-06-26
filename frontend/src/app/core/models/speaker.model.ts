export interface Speaker {
  id: number;
  name: string;
  email: string;
  bio?: string;
  photoUrl?: string;
  organization?: string;
}

export interface CreateSpeakerRequest {
  name: string;
  email: string;
  password: string;
  bio?: string;
  photoUrl?: string;
  organization?: string;
}
