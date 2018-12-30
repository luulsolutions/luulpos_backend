export interface IPaymentMethod {
  id?: number;
  paymentMethod?: string;
  description?: string;
  active?: boolean;
  shopShopName?: string;
  shopId?: number;
}

export const defaultValue: Readonly<IPaymentMethod> = {
  active: false
};
