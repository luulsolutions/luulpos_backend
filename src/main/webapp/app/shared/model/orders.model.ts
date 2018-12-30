import { Moment } from 'moment';
import { IOrdersLine } from 'app/shared/model//orders-line.model';

export const enum OrderStatus {
  INCOMPLETE = 'INCOMPLETE',
  COMPLETED = 'COMPLETED',
  PENDING = 'PENDING',
  READY = 'READY',
  CANCELLED = 'CANCELLED',
  REFUNDED = 'REFUNDED'
}

export interface IOrders {
  id?: number;
  description?: string;
  customerName?: string;
  totalPrice?: number;
  quantity?: number;
  discountPercentage?: number;
  discountAmount?: number;
  taxPercentage?: number;
  taxAmount?: number;
  orderStatus?: OrderStatus;
  note?: string;
  orderDate?: Moment;
  isVariantOrder?: boolean;
  ordersLines?: IOrdersLine[];
  paymentMethodPaymentMethod?: string;
  paymentMethodId?: number;
  soldByFirstName?: string;
  soldById?: number;
  preparedByFirstName?: string;
  preparedById?: number;
  shopDeviceDeviceName?: string;
  shopDeviceId?: number;
  sectionTableTableNumber?: string;
  sectionTableId?: number;
  shopShopName?: string;
  shopId?: number;
}

export const defaultValue: Readonly<IOrders> = {
  isVariantOrder: false
};
