import { IProduct } from 'app/shared/model//product.model';

export interface IProductCategory {
  id?: number;
  category?: string;
  description?: string;
  imageFullContentType?: string;
  imageFull?: any;
  imageFullUrl?: string;
  imageThumbContentType?: string;
  imageThumb?: any;
  imageThumbUrl?: string;
  shopShopName?: string;
  shopId?: number;
  products?: IProduct[];
}

export const defaultValue: Readonly<IProductCategory> = {};
