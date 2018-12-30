import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  Translate,
  translate,
  ICrudSearchAction,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './employee-timesheet.reducer';
import { IEmployeeTimesheet } from 'app/shared/model/employee-timesheet.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IEmployeeTimesheetProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IEmployeeTimesheetState extends IPaginationBaseState {
  search: string;
}

export class EmployeeTimesheet extends React.Component<IEmployeeTimesheetProps, IEmployeeTimesheetState> {
  state: IEmployeeTimesheetState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.props.getEntities();
    this.setState({
      search: ''
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { employeeTimesheetList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="employee-timesheet-heading">
          <Translate contentKey="luulposApp.employeeTimesheet.home.title">Employee Timesheets</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.employeeTimesheet.home.createLabel">Create new Employee Timesheet</Translate>
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput
                    type="text"
                    name="search"
                    value={this.state.search}
                    onChange={this.handleSearch}
                    placeholder={translate('luulposApp.employeeTimesheet.home.search')}
                  />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('checkinTime')}>
                  <Translate contentKey="luulposApp.employeeTimesheet.checkinTime">Checkin Time</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('checkOutTime')}>
                  <Translate contentKey="luulposApp.employeeTimesheet.checkOutTime">Check Out Time</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('regularHoursWorked')}>
                  <Translate contentKey="luulposApp.employeeTimesheet.regularHoursWorked">Regular Hours Worked</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('overTimeHoursWorked')}>
                  <Translate contentKey="luulposApp.employeeTimesheet.overTimeHoursWorked">Over Time Hours Worked</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('pay')}>
                  <Translate contentKey="luulposApp.employeeTimesheet.pay">Pay</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.employeeTimesheet.profile">Profile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.employeeTimesheet.shop">Shop</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {employeeTimesheetList.map((employeeTimesheet, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${employeeTimesheet.id}`} color="link" size="sm">
                      {employeeTimesheet.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={employeeTimesheet.checkinTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={employeeTimesheet.checkOutTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{employeeTimesheet.regularHoursWorked}</td>
                  <td>{employeeTimesheet.overTimeHoursWorked}</td>
                  <td>{employeeTimesheet.pay}</td>
                  <td>
                    {employeeTimesheet.profileFirstName ? (
                      <Link to={`profile/${employeeTimesheet.profileId}`}>{employeeTimesheet.profileFirstName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {employeeTimesheet.shopShopName ? (
                      <Link to={`shop/${employeeTimesheet.shopId}`}>{employeeTimesheet.shopShopName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${employeeTimesheet.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${employeeTimesheet.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${employeeTimesheet.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ employeeTimesheet }: IRootState) => ({
  employeeTimesheetList: employeeTimesheet.entities,
  totalItems: employeeTimesheet.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EmployeeTimesheet);
