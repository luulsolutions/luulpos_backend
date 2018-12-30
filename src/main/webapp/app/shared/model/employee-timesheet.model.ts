import { Moment } from 'moment';

export interface IEmployeeTimesheet {
  id?: number;
  checkinTime?: Moment;
  checkOutTime?: Moment;
  regularHoursWorked?: number;
  overTimeHoursWorked?: number;
  pay?: number;
  profileFirstName?: string;
  profileId?: number;
  shopShopName?: string;
  shopId?: number;
}

export const defaultValue: Readonly<IEmployeeTimesheet> = {};
