import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Company from './company';
import Shop from './shop';
import ShopSection from './shop-section';
import SectionTable from './section-table';
import SystemEventsHistory from './system-events-history';
import Product from './product';
import ProductCategory from './product-category';
import ProductVariant from './product-variant';
import ProductExtra from './product-extra';
import ProductType from './product-type';
import SystemConfig from './system-config';
import EmailBalancer from './email-balancer';
import Profile from './profile';
import EmployeeTimesheet from './employee-timesheet';
import Orders from './orders';
import OrdersLine from './orders-line';
import OrdersLineVariant from './orders-line-variant';
import OrdersLineExtra from './orders-line-extra';
import Discount from './discount';
import Tax from './tax';
import PaymentMethod from './payment-method';
import SuspensionHistory from './suspension-history';
import ShopDevice from './shop-device';
import PaymentMethodConfig from './payment-method-config';
import Payment from './payment';
import ShopChange from './shop-change';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/company`} component={Company} />
      <ErrorBoundaryRoute path={`${match.url}/shop`} component={Shop} />
      <ErrorBoundaryRoute path={`${match.url}/shop-section`} component={ShopSection} />
      <ErrorBoundaryRoute path={`${match.url}/section-table`} component={SectionTable} />
      <ErrorBoundaryRoute path={`${match.url}/system-events-history`} component={SystemEventsHistory} />
      <ErrorBoundaryRoute path={`${match.url}/product`} component={Product} />
      <ErrorBoundaryRoute path={`${match.url}/product-category`} component={ProductCategory} />
      <ErrorBoundaryRoute path={`${match.url}/product-variant`} component={ProductVariant} />
      <ErrorBoundaryRoute path={`${match.url}/product-extra`} component={ProductExtra} />
      <ErrorBoundaryRoute path={`${match.url}/product-type`} component={ProductType} />
      <ErrorBoundaryRoute path={`${match.url}/system-config`} component={SystemConfig} />
      <ErrorBoundaryRoute path={`${match.url}/email-balancer`} component={EmailBalancer} />
      <ErrorBoundaryRoute path={`${match.url}/profile`} component={Profile} />
      <ErrorBoundaryRoute path={`${match.url}/employee-timesheet`} component={EmployeeTimesheet} />
      <ErrorBoundaryRoute path={`${match.url}/orders`} component={Orders} />
      <ErrorBoundaryRoute path={`${match.url}/orders-line`} component={OrdersLine} />
      <ErrorBoundaryRoute path={`${match.url}/orders-line-variant`} component={OrdersLineVariant} />
      <ErrorBoundaryRoute path={`${match.url}/orders-line-extra`} component={OrdersLineExtra} />
      <ErrorBoundaryRoute path={`${match.url}/discount`} component={Discount} />
      <ErrorBoundaryRoute path={`${match.url}/tax`} component={Tax} />
      <ErrorBoundaryRoute path={`${match.url}/payment-method`} component={PaymentMethod} />
      <ErrorBoundaryRoute path={`${match.url}/suspension-history`} component={SuspensionHistory} />
      <ErrorBoundaryRoute path={`${match.url}/shop-device`} component={ShopDevice} />
      <ErrorBoundaryRoute path={`${match.url}/payment-method-config`} component={PaymentMethodConfig} />
      <ErrorBoundaryRoute path={`${match.url}/payment`} component={Payment} />
      <ErrorBoundaryRoute path={`${match.url}/shop-change`} component={ShopChange} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
