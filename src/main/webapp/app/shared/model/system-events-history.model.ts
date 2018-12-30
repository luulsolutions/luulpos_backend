import { Moment } from 'moment';

export interface ISystemEventsHistory {
  id?: number;
  eventName?: string;
  eventDate?: Moment;
  eventApi?: string;
  eventNote?: string;
  eventEntityName?: string;
  eventEntityId?: number;
  triggedByFirstName?: string;
  triggedById?: number;
}

export const defaultValue: Readonly<ISystemEventsHistory> = {};
