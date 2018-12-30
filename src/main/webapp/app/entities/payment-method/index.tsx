import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PaymentMethod from './payment-method';
import PaymentMethodDetail from './payment-method-detail';
import PaymentMethodUpdate from './payment-method-update';
import PaymentMethodDeleteDialog from './payment-method-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaymentMethodUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaymentMethodUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaymentMethodDetail} />
      <ErrorBoundaryRoute path={match.url} component={PaymentMethod} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PaymentMethodDeleteDialog} />
  </>
);

export default Routes;
