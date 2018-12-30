import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SectionTable from './section-table';
import SectionTableDetail from './section-table-detail';
import SectionTableUpdate from './section-table-update';
import SectionTableDeleteDialog from './section-table-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SectionTableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SectionTableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SectionTableDetail} />
      <ErrorBoundaryRoute path={match.url} component={SectionTable} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SectionTableDeleteDialog} />
  </>
);

export default Routes;
