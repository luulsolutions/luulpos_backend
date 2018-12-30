import { Moment } from 'moment';
import { IProductVariant } from 'app/shared/model//product-variant.model';
import { IProductExtra } from 'app/shared/model//product-extra.model';

export interface IProduct {
  id?: number;
  productName?: string;
  productDescription?: string;
  price?: number;
  quantity?: number;
  productImageFullContentType?: string;
  productImageFull?: any;
  productImageFullUrl?: string;
  productImageThumbContentType?: string;
  productImageThumb?: any;
  productImageThumbUrl?: string;
  dateCreated?: Moment;
  barcode?: string;
  serialCode?: string;
  priorityPosition?: number;
  active?: boolean;
  isVariantProduct?: boolean;
  variants?: IProductVariant[];
  extras?: IProductExtra[];
  productTypesProductType?: string;
  productTypesId?: number;
  shopShopName?: string;
  shopId?: number;
  discountsDescription?: string;
  discountsId?: number;
  taxesDescription?: string;
  taxesId?: number;
  categoryCategory?: string;
  categoryId?: number;
}

export const defaultValue: Readonly<IProduct> = {
  active: false,
  isVariantProduct: false
};
