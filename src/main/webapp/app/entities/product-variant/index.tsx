import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductVariant from './product-variant';
import ProductVariantDetail from './product-variant-detail';
import ProductVariantUpdate from './product-variant-update';
import ProductVariantDeleteDialog from './product-variant-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductVariantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductVariantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductVariantDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductVariant} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ProductVariantDeleteDialog} />
  </>
);

export default Routes;
