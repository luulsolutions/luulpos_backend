import { Moment } from 'moment';
import { IProfile } from 'app/shared/model//profile.model';
import { IProductCategory } from 'app/shared/model//product-category.model';
import { IProductType } from 'app/shared/model//product-type.model';
import { ISystemConfig } from 'app/shared/model//system-config.model';
import { IDiscount } from 'app/shared/model//discount.model';
import { ITax } from 'app/shared/model//tax.model';

export const enum ShopAccountType {
  TRIAL_ACCOUNT = 'TRIAL_ACCOUNT',
  SILVER_ACCOUNT = 'SILVER_ACCOUNT',
  GOLD_ACCOUNT = 'GOLD_ACCOUNT'
}

export interface IShop {
  id?: number;
  shopName?: string;
  shopAccountType?: ShopAccountType;
  approvalDate?: Moment;
  address?: string;
  email?: string;
  description?: string;
  note?: string;
  landland?: string;
  mobile?: string;
  createdDate?: Moment;
  shopLogoContentType?: string;
  shopLogo?: any;
  shopLogoUrl?: string;
  active?: boolean;
  currency?: string;
  companyCompanyName?: string;
  companyId?: number;
  approvedByFirstName?: string;
  approvedById?: number;
  profiles?: IProfile[];
  productCategories?: IProductCategory[];
  productTypes?: IProductType[];
  systemConfigs?: ISystemConfig[];
  discounts?: IDiscount[];
  taxes?: ITax[];
}

export const defaultValue: Readonly<IShop> = {
  active: false
};
