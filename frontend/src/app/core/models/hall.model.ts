export interface Hall {
  id: number;
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
