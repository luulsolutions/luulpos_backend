import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './orders.reducer';
import { IOrders } from 'app/shared/model/orders.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrdersDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrdersDetail extends React.Component<IOrdersDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ordersEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.orders.detail.title">Orders</Translate> [<b>{ordersEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.orders.description">Description</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.description}</dd>
            <dt>
              <span id="customerName">
                <Translate contentKey="luulposApp.orders.customerName">Customer Name</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.customerName}</dd>
            <dt>
              <span id="totalPrice">
                <Translate contentKey="luulposApp.orders.totalPrice">Total Price</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.totalPrice}</dd>
            <dt>
              <span id="quantity">
                <Translate contentKey="luulposApp.orders.quantity">Quantity</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.quantity}</dd>
            <dt>
              <span id="discountPercentage">
                <Translate contentKey="luulposApp.orders.discountPercentage">Discount Percentage</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.discountPercentage}</dd>
            <dt>
              <span id="discountAmount">
                <Translate contentKey="luulposApp.orders.discountAmount">Discount Amount</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.discountAmount}</dd>
            <dt>
              <span id="taxPercentage">
                <Translate contentKey="luulposApp.orders.taxPercentage">Tax Percentage</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.taxPercentage}</dd>
            <dt>
              <span id="taxAmount">
                <Translate contentKey="luulposApp.orders.taxAmount">Tax Amount</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.taxAmount}</dd>
            <dt>
              <span id="orderStatus">
                <Translate contentKey="luulposApp.orders.orderStatus">Order Status</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.orderStatus}</dd>
            <dt>
              <span id="note">
                <Translate contentKey="luulposApp.orders.note">Note</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.note}</dd>
            <dt>
              <span id="orderDate">
                <Translate contentKey="luulposApp.orders.orderDate">Order Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={ordersEntity.orderDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="isVariantOrder">
                <Translate contentKey="luulposApp.orders.isVariantOrder">Is Variant Order</Translate>
              </span>
            </dt>
            <dd>{ordersEntity.isVariantOrder ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.orders.paymentMethod">Payment Method</Translate>
            </dt>
            <dd>{ordersEntity.paymentMethodPaymentMethod ? ordersEntity.paymentMethodPaymentMethod : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.orders.soldBy">Sold By</Translate>
            </dt>
            <dd>{ordersEntity.soldByFirstName ? ordersEntity.soldByFirstName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.orders.preparedBy">Prepared By</Translate>
            </dt>
            <dd>{ordersEntity.preparedByFirstName ? ordersEntity.preparedByFirstName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.orders.shopDevice">Shop Device</Translate>
            </dt>
            <dd>{ordersEntity.shopDeviceDeviceName ? ordersEntity.shopDeviceDeviceName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.orders.sectionTable">Section Table</Translate>
            </dt>
            <dd>{ordersEntity.sectionTableTableNumber ? ordersEntity.sectionTableTableNumber : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.orders.shop">Shop</Translate>
            </dt>
            <dd>{ordersEntity.shopShopName ? ordersEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/orders" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/orders/${ordersEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ orders }: IRootState) => ({
  ordersEntity: orders.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrdersDetail);
