export interface IProductExtra {
  id?: number;
  extraName?: string;
  description?: string;
  extraValue?: number;
  fullPhotoContentType?: string;
  fullPhoto?: any;
  fullPhotoUrl?: string;
  thumbnailPhotoContentType?: string;
  thumbnailPhoto?: any;
  thumbnailPhotoUrl?: string;
  productProductName?: string;
  productId?: number;
}

export const defaultValue: Readonly<IProductExtra> = {};
