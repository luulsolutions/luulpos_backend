import { Moment } from 'moment';

export const enum PaymentStatus {
  PENDING = 'PENDING',
  PAID = 'PAID',
  CANCELLED = 'CANCELLED',
  REFUNDED = 'REFUNDED',
  FAILED = 'FAILED'
}

export interface IPayment {
  id?: number;
  paymentDate?: Moment;
  paymentProvider?: string;
  amount?: number;
  paymentStatus?: PaymentStatus;
  curency?: string;
  customerName?: string;
  shopShopName?: string;
  shopId?: number;
  processedByFirstName?: string;
  processedById?: number;
  paymentMethodPaymentMethod?: string;
  paymentMethodId?: number;
  orderDescription?: string;
  orderId?: number;
}

export const defaultValue: Readonly<IPayment> = {};
