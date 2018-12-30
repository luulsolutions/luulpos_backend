import { Moment } from 'moment';

export interface IShopDevice {
  id?: number;
  deviceName?: string;
  deviceModel?: string;
  registeredDate?: Moment;
  shopShopName?: string;
  shopId?: number;
}

export const defaultValue: Readonly<IShopDevice> = {};
