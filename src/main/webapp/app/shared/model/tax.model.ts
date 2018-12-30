import { IProduct } from 'app/shared/model//product.model';

export interface ITax {
  id?: number;
  description?: string;
  percentage?: number;
  amount?: number;
  active?: boolean;
  shopShopName?: string;
  shopId?: number;
  products?: IProduct[];
}

export const defaultValue: Readonly<ITax> = {
  active: false
};
