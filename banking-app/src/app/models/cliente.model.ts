import { Person } from './person.model';

export interface Cliente extends Person {
  id?: number;

  clientState?: boolean;
  clientPassword: string;
  estado: boolean;
}
