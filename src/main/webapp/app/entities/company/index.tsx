import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Company from './company';
import CompanyDetail from './company-detail';
import CompanyUpdate from './company-update';
import CompanyDeleteDialog from './company-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CompanyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CompanyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CompanyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Company} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CompanyDeleteDialog} />
  </>
);

export default Routes;
