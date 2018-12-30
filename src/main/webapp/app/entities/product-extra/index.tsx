import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductExtra from './product-extra';
import ProductExtraDetail from './product-extra-detail';
import ProductExtraUpdate from './product-extra-update';
import ProductExtraDeleteDialog from './product-extra-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductExtraDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductExtra} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ProductExtraDeleteDialog} />
  </>
);

export default Routes;
