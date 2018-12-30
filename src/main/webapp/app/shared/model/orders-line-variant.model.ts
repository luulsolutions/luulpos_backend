import { IOrdersLineExtra } from 'app/shared/model//orders-line-extra.model';

export interface IOrdersLineVariant {
  id?: number;
  variantName?: string;
  variantValue?: string;
  description?: string;
  percentage?: number;
  fullPhotoContentType?: string;
  fullPhoto?: any;
  fullPhotoUrl?: string;
  thumbnailPhotoContentType?: string;
  thumbnailPhoto?: any;
  thumbnailPhotoUrl?: string;
  price?: number;
  ordersLineId?: number;
  ordersLineExtras?: IOrdersLineExtra[];
}

export const defaultValue: Readonly<IOrdersLineVariant> = {};
