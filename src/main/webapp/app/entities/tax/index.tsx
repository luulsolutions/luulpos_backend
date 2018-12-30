import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tax from './tax';
import TaxDetail from './tax-detail';
import TaxUpdate from './tax-update';
import TaxDeleteDialog from './tax-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaxUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaxUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaxDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tax} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TaxDeleteDialog} />
  </>
);

export default Routes;
