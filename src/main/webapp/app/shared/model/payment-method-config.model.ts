export interface IPaymentMethodConfig {
  id?: number;
  key?: string;
  value?: string;
  note?: string;
  enabled?: boolean;
  paymentMethodPaymentMethod?: string;
  paymentMethodId?: number;
}

export const defaultValue: Readonly<IPaymentMethodConfig> = {
  enabled: false
};
