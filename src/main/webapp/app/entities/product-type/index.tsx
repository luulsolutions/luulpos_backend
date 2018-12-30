import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductType from './product-type';
import ProductTypeDetail from './product-type-detail';
import ProductTypeUpdate from './product-type-update';
import ProductTypeDeleteDialog from './product-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductType} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ProductTypeDeleteDialog} />
  </>
);

export default Routes;
