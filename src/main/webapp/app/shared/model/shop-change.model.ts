import { Moment } from 'moment';

export interface IShopChange {
  id?: number;
  change?: string;
  changedEntity?: string;
  note?: string;
  changeDate?: Moment;
  shopShopName?: string;
  shopId?: number;
  changedByFirstName?: string;
  changedById?: number;
}

export const defaultValue: Readonly<IShopChange> = {};
