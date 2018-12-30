import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import company, {
  CompanyState
} from 'app/entities/company/company.reducer';
// prettier-ignore
import shop, {
  ShopState
} from 'app/entities/shop/shop.reducer';
// prettier-ignore
import shopSection, {
  ShopSectionState
} from 'app/entities/shop-section/shop-section.reducer';
// prettier-ignore
import sectionTable, {
  SectionTableState
} from 'app/entities/section-table/section-table.reducer';
// prettier-ignore
import systemEventsHistory, {
  SystemEventsHistoryState
} from 'app/entities/system-events-history/system-events-history.reducer';
// prettier-ignore
import product, {
  ProductState
} from 'app/entities/product/product.reducer';
// prettier-ignore
import productCategory, {
  ProductCategoryState
} from 'app/entities/product-category/product-category.reducer';
// prettier-ignore
import productVariant, {
  ProductVariantState
} from 'app/entities/product-variant/product-variant.reducer';
// prettier-ignore
import productExtra, {
  ProductExtraState
} from 'app/entities/product-extra/product-extra.reducer';
// prettier-ignore
import productType, {
  ProductTypeState
} from 'app/entities/product-type/product-type.reducer';
// prettier-ignore
import systemConfig, {
  SystemConfigState
} from 'app/entities/system-config/system-config.reducer';
// prettier-ignore
import emailBalancer, {
  EmailBalancerState
} from 'app/entities/email-balancer/email-balancer.reducer';
// prettier-ignore
import profile, {
  ProfileState
} from 'app/entities/profile/profile.reducer';
// prettier-ignore
import employeeTimesheet, {
  EmployeeTimesheetState
} from 'app/entities/employee-timesheet/employee-timesheet.reducer';
// prettier-ignore
import orders, {
  OrdersState
} from 'app/entities/orders/orders.reducer';
// prettier-ignore
import ordersLine, {
  OrdersLineState
} from 'app/entities/orders-line/orders-line.reducer';
// prettier-ignore
import ordersLineVariant, {
  OrdersLineVariantState
} from 'app/entities/orders-line-variant/orders-line-variant.reducer';
// prettier-ignore
import ordersLineExtra, {
  OrdersLineExtraState
} from 'app/entities/orders-line-extra/orders-line-extra.reducer';
// prettier-ignore
import discount, {
  DiscountState
} from 'app/entities/discount/discount.reducer';
// prettier-ignore
import tax, {
  TaxState
} from 'app/entities/tax/tax.reducer';
// prettier-ignore
import paymentMethod, {
  PaymentMethodState
} from 'app/entities/payment-method/payment-method.reducer';
// prettier-ignore
import suspensionHistory, {
  SuspensionHistoryState
} from 'app/entities/suspension-history/suspension-history.reducer';
// prettier-ignore
import shopDevice, {
  ShopDeviceState
} from 'app/entities/shop-device/shop-device.reducer';
// prettier-ignore
import paymentMethodConfig, {
  PaymentMethodConfigState
} from 'app/entities/payment-method-config/payment-method-config.reducer';
// prettier-ignore
import payment, {
  PaymentState
} from 'app/entities/payment/payment.reducer';
// prettier-ignore
import shopChange, {
  ShopChangeState
} from 'app/entities/shop-change/shop-change.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly company: CompanyState;
  readonly shop: ShopState;
  readonly shopSection: ShopSectionState;
  readonly sectionTable: SectionTableState;
  readonly systemEventsHistory: SystemEventsHistoryState;
  readonly product: ProductState;
  readonly productCategory: ProductCategoryState;
  readonly productVariant: ProductVariantState;
  readonly productExtra: ProductExtraState;
  readonly productType: ProductTypeState;
  readonly systemConfig: SystemConfigState;
  readonly emailBalancer: EmailBalancerState;
  readonly profile: ProfileState;
  readonly employeeTimesheet: EmployeeTimesheetState;
  readonly orders: OrdersState;
  readonly ordersLine: OrdersLineState;
  readonly ordersLineVariant: OrdersLineVariantState;
  readonly ordersLineExtra: OrdersLineExtraState;
  readonly discount: DiscountState;
  readonly tax: TaxState;
  readonly paymentMethod: PaymentMethodState;
  readonly suspensionHistory: SuspensionHistoryState;
  readonly shopDevice: ShopDeviceState;
  readonly paymentMethodConfig: PaymentMethodConfigState;
  readonly payment: PaymentState;
  readonly shopChange: ShopChangeState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  company,
  shop,
  shopSection,
  sectionTable,
  systemEventsHistory,
  product,
  productCategory,
  productVariant,
  productExtra,
  productType,
  systemConfig,
  emailBalancer,
  profile,
  employeeTimesheet,
  orders,
  ordersLine,
  ordersLineVariant,
  ordersLineExtra,
  discount,
  tax,
  paymentMethod,
  suspensionHistory,
  shopDevice,
  paymentMethodConfig,
  payment,
  shopChange,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
