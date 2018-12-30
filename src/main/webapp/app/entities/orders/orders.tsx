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
import { getSearchEntities, getEntities } from './orders.reducer';
import { IOrders } from 'app/shared/model/orders.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IOrdersProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IOrdersState extends IPaginationBaseState {
  search: string;
}

export class Orders extends React.Component<IOrdersProps, IOrdersState> {
  state: IOrdersState = {
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
    const { ordersList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="orders-heading">
          <Translate contentKey="luulposApp.orders.home.title">Orders</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.orders.home.createLabel">Create new Orders</Translate>
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
                    placeholder={translate('luulposApp.orders.home.search')}
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
                <th className="hand" onClick={this.sort('description')}>
                  <Translate contentKey="luulposApp.orders.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('customerName')}>
                  <Translate contentKey="luulposApp.orders.customerName">Customer Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('totalPrice')}>
                  <Translate contentKey="luulposApp.orders.totalPrice">Total Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('quantity')}>
                  <Translate contentKey="luulposApp.orders.quantity">Quantity</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('discountPercentage')}>
                  <Translate contentKey="luulposApp.orders.discountPercentage">Discount Percentage</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('discountAmount')}>
                  <Translate contentKey="luulposApp.orders.discountAmount">Discount Amount</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('taxPercentage')}>
                  <Translate contentKey="luulposApp.orders.taxPercentage">Tax Percentage</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('taxAmount')}>
                  <Translate contentKey="luulposApp.orders.taxAmount">Tax Amount</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('orderStatus')}>
                  <Translate contentKey="luulposApp.orders.orderStatus">Order Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('note')}>
                  <Translate contentKey="luulposApp.orders.note">Note</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('orderDate')}>
                  <Translate contentKey="luulposApp.orders.orderDate">Order Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('isVariantOrder')}>
                  <Translate contentKey="luulposApp.orders.isVariantOrder">Is Variant Order</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.orders.paymentMethod">Payment Method</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.orders.soldBy">Sold By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.orders.preparedBy">Prepared By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.orders.shopDevice">Shop Device</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.orders.sectionTable">Section Table</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.orders.shop">Shop</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ordersList.map((orders, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${orders.id}`} color="link" size="sm">
                      {orders.id}
                    </Button>
                  </td>
                  <td>{orders.description}</td>
                  <td>{orders.customerName}</td>
                  <td>{orders.totalPrice}</td>
                  <td>{orders.quantity}</td>
                  <td>{orders.discountPercentage}</td>
                  <td>{orders.discountAmount}</td>
                  <td>{orders.taxPercentage}</td>
                  <td>{orders.taxAmount}</td>
                  <td>
                    <Translate contentKey={`luulposApp.OrderStatus.${orders.orderStatus}`} />
                  </td>
                  <td>{orders.note}</td>
                  <td>
                    <TextFormat type="date" value={orders.orderDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{orders.isVariantOrder ? 'true' : 'false'}</td>
                  <td>
                    {orders.paymentMethodPaymentMethod ? (
                      <Link to={`payment-method/${orders.paymentMethodId}`}>{orders.paymentMethodPaymentMethod}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{orders.soldByFirstName ? <Link to={`profile/${orders.soldById}`}>{orders.soldByFirstName}</Link> : ''}</td>
                  <td>
                    {orders.preparedByFirstName ? <Link to={`profile/${orders.preparedById}`}>{orders.preparedByFirstName}</Link> : ''}
                  </td>
                  <td>
                    {orders.shopDeviceDeviceName ? (
                      <Link to={`shop-device/${orders.shopDeviceId}`}>{orders.shopDeviceDeviceName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {orders.sectionTableTableNumber ? (
                      <Link to={`section-table/${orders.sectionTableId}`}>{orders.sectionTableTableNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{orders.shopShopName ? <Link to={`shop/${orders.shopId}`}>{orders.shopShopName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${orders.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${orders.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${orders.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ orders }: IRootState) => ({
  ordersList: orders.entities,
  totalItems: orders.totalItems
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
)(Orders);
