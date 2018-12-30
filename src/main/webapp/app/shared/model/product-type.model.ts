export interface IProductType {
  id?: number;
  productType?: string;
  productTypeDescription?: string;
  shopShopName?: string;
  shopId?: number;
}

export const defaultValue: Readonly<IProductType> = {};
