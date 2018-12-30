import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PaymentMethodConfig from './payment-method-config';
import PaymentMethodConfigDetail from './payment-method-config-detail';
import PaymentMethodConfigUpdate from './payment-method-config-update';
import PaymentMethodConfigDeleteDialog from './payment-method-config-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaymentMethodConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaymentMethodConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaymentMethodConfigDetail} />
      <ErrorBoundaryRoute path={match.url} component={PaymentMethodConfig} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PaymentMethodConfigDeleteDialog} />
  </>
);

export default Routes;
