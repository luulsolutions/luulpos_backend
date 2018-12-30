import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrdersLineVariant from './orders-line-variant';
import OrdersLineVariantDetail from './orders-line-variant-detail';
import OrdersLineVariantUpdate from './orders-line-variant-update';
import OrdersLineVariantDeleteDialog from './orders-line-variant-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrdersLineVariantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrdersLineVariantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrdersLineVariantDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrdersLineVariant} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrdersLineVariantDeleteDialog} />
  </>
);

export default Routes;
