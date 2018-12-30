import { Moment } from 'moment';

export const enum SuspensionType {
  BANNED_FOR_LIFE = 'BANNED_FOR_LIFE',
  BANNED_TEMPORARILY = 'BANNED_TEMPORARILY',
  DELETED_BY_USER = 'DELETED_BY_USER',
  DELETED_BY_ADMIN = 'DELETED_BY_ADMIN',
  TEMP_DEACTIVATION_BY_USER = 'TEMP_DEACTIVATION_BY_USER',
  UNDER_INVESTIGATION = 'UNDER_INVESTIGATION',
  NONE = 'NONE'
}

export interface ISuspensionHistory {
  id?: number;
  suspendedDate?: Moment;
  suspensionType?: SuspensionType;
  suspendedReason?: string;
  resolutionNote?: string;
  unsuspensionDate?: Moment;
  profileFirstName?: string;
  profileId?: number;
  suspendedByFirstName?: string;
  suspendedById?: number;
}

export const defaultValue: Readonly<ISuspensionHistory> = {};
