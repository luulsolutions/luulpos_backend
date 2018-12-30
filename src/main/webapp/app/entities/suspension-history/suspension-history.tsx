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
import { getSearchEntities, getEntities } from './suspension-history.reducer';
import { ISuspensionHistory } from 'app/shared/model/suspension-history.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ISuspensionHistoryProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ISuspensionHistoryState extends IPaginationBaseState {
  search: string;
}

export class SuspensionHistory extends React.Component<ISuspensionHistoryProps, ISuspensionHistoryState> {
  state: ISuspensionHistoryState = {
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
    const { suspensionHistoryList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="suspension-history-heading">
          <Translate contentKey="luulposApp.suspensionHistory.home.title">Suspension Histories</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.suspensionHistory.home.createLabel">Create new Suspension History</Translate>
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
                    placeholder={translate('luulposApp.suspensionHistory.home.search')}
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
                <th className="hand" onClick={this.sort('suspendedDate')}>
                  <Translate contentKey="luulposApp.suspensionHistory.suspendedDate">Suspended Date</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('suspensionType')}>
                  <Translate contentKey="luulposApp.suspensionHistory.suspensionType">Suspension Type</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('suspendedReason')}>
                  <Translate contentKey="luulposApp.suspensionHistory.suspendedReason">Suspended Reason</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('resolutionNote')}>
                  <Translate contentKey="luulposApp.suspensionHistory.resolutionNote">Resolution Note</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('unsuspensionDate')}>
                  <Translate contentKey="luulposApp.suspensionHistory.unsuspensionDate">Unsuspension Date</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.suspensionHistory.profile">Profile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.suspensionHistory.suspendedBy">Suspended By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {suspensionHistoryList.map((suspensionHistory, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${suspensionHistory.id}`} color="link" size="sm">
                      {suspensionHistory.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={suspensionHistory.suspendedDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <Translate contentKey={`luulposApp.SuspensionType.${suspensionHistory.suspensionType}`} />
                  </td>
                  <td>{suspensionHistory.suspendedReason}</td>
                  <td>{suspensionHistory.resolutionNote}</td>
                  <td>
                    <TextFormat type="date" value={suspensionHistory.unsuspensionDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    {suspensionHistory.profileFirstName ? (
                      <Link to={`profile/${suspensionHistory.profileId}`}>{suspensionHistory.profileFirstName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {suspensionHistory.suspendedByFirstName ? (
                      <Link to={`profile/${suspensionHistory.suspendedById}`}>{suspensionHistory.suspendedByFirstName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${suspensionHistory.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${suspensionHistory.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${suspensionHistory.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ suspensionHistory }: IRootState) => ({
  suspensionHistoryList: suspensionHistory.entities,
  totalItems: suspensionHistory.totalItems
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
)(SuspensionHistory);
