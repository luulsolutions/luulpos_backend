import { IOrdersLineVariant } from 'app/shared/model//orders-line-variant.model';

export interface IOrdersLine {
  id?: number;
  ordersLineName?: string;
  ordersLineValue?: string;
  ordersLinePrice?: number;
  ordersLineDescription?: string;
  thumbnailPhotoContentType?: string;
  thumbnailPhoto?: any;
  fullPhotoContentType?: string;
  fullPhoto?: any;
  fullPhotoUrl?: string;
  thumbnailPhotoUrl?: string;
  ordersId?: number;
  ordersLineVariants?: IOrdersLineVariant[];
  productProductName?: string;
  productId?: number;
}

export const defaultValue: Readonly<IOrdersLine> = {};
