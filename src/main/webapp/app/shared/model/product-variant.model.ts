export interface IProductVariant {
  id?: number;
  variantName?: string;
  description?: string;
  percentage?: number;
  fullPhotoContentType?: string;
  fullPhoto?: any;
  fullPhotoUrl?: string;
  thumbnailPhotoContentType?: string;
  thumbnailPhoto?: any;
  thumbnailPhotoUrl?: string;
  price?: number;
  productProductName?: string;
  productId?: number;
}

export const defaultValue: Readonly<IProductVariant> = {};
