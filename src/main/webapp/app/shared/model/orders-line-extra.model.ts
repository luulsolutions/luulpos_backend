export interface IOrdersLineExtra {
  id?: number;
  ordersLineExtraName?: string;
  ordersLineExtraValue?: string;
  ordersLineExtraPrice?: number;
  ordersOptionDescription?: string;
  fullPhotoContentType?: string;
  fullPhoto?: any;
  fullPhotoUrl?: string;
  thumbnailPhotoContentType?: string;
  thumbnailPhoto?: any;
  thumbnailPhotoUrl?: string;
  ordersLineVariantId?: number;
}

export const defaultValue: Readonly<IOrdersLineExtra> = {};
