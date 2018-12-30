import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Profile from './profile';
import ProfileDetail from './profile-detail';
import ProfileUpdate from './profile-update';
import ProfileDeleteDialog from './profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={Profile} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ProfileDeleteDialog} />
  </>
);

export default Routes;
