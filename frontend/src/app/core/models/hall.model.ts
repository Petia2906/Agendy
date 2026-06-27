export interface Hall {
  id: string;
  eventId: string;
  name: string;
  capacity: number;
}

export interface CreateHallRequest {
  name: string;
  capacity: number;
}

export interface UpdateHallRequest {
  name: string;
  capacity: number;
}
