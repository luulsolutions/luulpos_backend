import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  openFile,
  byteSize,
  Translate,
  translate,
  ICrudSearchAction,
  ICrudGetAllAction,
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './orders-line.reducer';
import { IOrdersLine } from 'app/shared/model/orders-line.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IOrdersLineProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IOrdersLineState extends IPaginationBaseState {
  search: string;
}

export class OrdersLine extends React.Component<IOrdersLineProps, IOrdersLineState> {
  state: IOrdersLineState = {
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
    const { ordersLineList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="orders-line-heading">
          <Translate contentKey="luulposApp.ordersLine.home.title">Orders Lines</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.ordersLine.home.createLabel">Create new Orders Line</Translate>
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
                    placeholder={translate('luulposApp.ordersLine.home.search')}
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
                <th className="hand" onClick={this.sort('ordersLineName')}>
                  <Translate contentKey="luulposApp.ordersLine.ordersLineName">Orders Line Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('ordersLineValue')}>
                  <Translate contentKey="luulposApp.ordersLine.ordersLineValue">Orders Line Value</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('ordersLinePrice')}>
                  <Translate contentKey="luulposApp.ordersLine.ordersLinePrice">Orders Line Price</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('ordersLineDescription')}>
                  <Translate contentKey="luulposApp.ordersLine.ordersLineDescription">Orders Line Description</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhoto')}>
                  <Translate contentKey="luulposApp.ordersLine.thumbnailPhoto">Thumbnail Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhoto')}>
                  <Translate contentKey="luulposApp.ordersLine.fullPhoto">Full Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhotoUrl')}>
                  <Translate contentKey="luulposApp.ordersLine.fullPhotoUrl">Full Photo Url</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhotoUrl')}>
                  <Translate contentKey="luulposApp.ordersLine.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.ordersLine.orders">Orders</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.ordersLine.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ordersLineList.map((ordersLine, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${ordersLine.id}`} color="link" size="sm">
                      {ordersLine.id}
                    </Button>
                  </td>
                  <td>{ordersLine.ordersLineName}</td>
                  <td>{ordersLine.ordersLineValue}</td>
                  <td>{ordersLine.ordersLinePrice}</td>
                  <td>{ordersLine.ordersLineDescription}</td>
                  <td>
                    {ordersLine.thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(ordersLine.thumbnailPhotoContentType, ordersLine.thumbnailPhoto)}>
                          <img
                            src={`data:${ordersLine.thumbnailPhotoContentType};base64,${ordersLine.thumbnailPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {ordersLine.thumbnailPhotoContentType}, {byteSize(ordersLine.thumbnailPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {ordersLine.fullPhoto ? (
                      <div>
                        <a onClick={openFile(ordersLine.fullPhotoContentType, ordersLine.fullPhoto)}>
                          <img
                            src={`data:${ordersLine.fullPhotoContentType};base64,${ordersLine.fullPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {ordersLine.fullPhotoContentType}, {byteSize(ordersLine.fullPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{ordersLine.fullPhotoUrl}</td>
                  <td>{ordersLine.thumbnailPhotoUrl}</td>
                  <td>{ordersLine.ordersId ? <Link to={`orders/${ordersLine.ordersId}`}>{ordersLine.ordersId}</Link> : ''}</td>
                  <td>
                    {ordersLine.productProductName ? (
                      <Link to={`product/${ordersLine.productId}`}>{ordersLine.productProductName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${ordersLine.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ordersLine.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ordersLine.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ ordersLine }: IRootState) => ({
  ordersLineList: ordersLine.entities,
  totalItems: ordersLine.totalItems
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
)(OrdersLine);
