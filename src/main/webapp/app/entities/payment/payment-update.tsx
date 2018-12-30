import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { getEntities as getPaymentMethods } from 'app/entities/payment-method/payment-method.reducer';
import { IOrders } from 'app/shared/model/orders.model';
import { getEntities as getOrders } from 'app/entities/orders/orders.reducer';
import { getEntity, updateEntity, createEntity, reset } from './payment.reducer';
import { IPayment } from 'app/shared/model/payment.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPaymentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPaymentUpdateState {
  isNew: boolean;
  shopId: string;
  processedById: string;
  paymentMethodId: string;
  orderId: string;
}

export class PaymentUpdate extends React.Component<IPaymentUpdateProps, IPaymentUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      shopId: '0',
      processedById: '0',
      paymentMethodId: '0',
      orderId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getShops();
    this.props.getProfiles();
    this.props.getPaymentMethods();
    this.props.getOrders();
  }

  saveEntity = (event, errors, values) => {
    values.paymentDate = new Date(values.paymentDate);

    if (errors.length === 0) {
      const { paymentEntity } = this.props;
      const entity = {
        ...paymentEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/payment');
  };

  render() {
    const { paymentEntity, shops, profiles, paymentMethods, orders, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.payment.home.createOrEditLabel">
              <Translate contentKey="luulposApp.payment.home.createOrEditLabel">Create or edit a Payment</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : paymentEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="payment-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="paymentDateLabel" for="paymentDate">
                    <Translate contentKey="luulposApp.payment.paymentDate">Payment Date</Translate>
                  </Label>
                  <AvInput
                    id="payment-paymentDate"
                    type="datetime-local"
                    className="form-control"
                    name="paymentDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.paymentEntity.paymentDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentProviderLabel" for="paymentProvider">
                    <Translate contentKey="luulposApp.payment.paymentProvider">Payment Provider</Translate>
                  </Label>
                  <AvField id="payment-paymentProvider" type="text" name="paymentProvider" />
                </AvGroup>
                <AvGroup>
                  <Label id="amountLabel" for="amount">
                    <Translate contentKey="luulposApp.payment.amount">Amount</Translate>
                  </Label>
                  <AvField id="payment-amount" type="text" name="amount" />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentStatusLabel">
                    <Translate contentKey="luulposApp.payment.paymentStatus">Payment Status</Translate>
                  </Label>
                  <AvInput
                    id="payment-paymentStatus"
                    type="select"
                    className="form-control"
                    name="paymentStatus"
                    value={(!isNew && paymentEntity.paymentStatus) || 'PENDING'}
                  >
                    <option value="PENDING">
                      <Translate contentKey="luulposApp.PaymentStatus.PENDING" />
                    </option>
                    <option value="PAID">
                      <Translate contentKey="luulposApp.PaymentStatus.PAID" />
                    </option>
                    <option value="CANCELLED">
                      <Translate contentKey="luulposApp.PaymentStatus.CANCELLED" />
                    </option>
                    <option value="REFUNDED">
                      <Translate contentKey="luulposApp.PaymentStatus.REFUNDED" />
                    </option>
                    <option value="FAILED">
                      <Translate contentKey="luulposApp.PaymentStatus.FAILED" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="curencyLabel" for="curency">
                    <Translate contentKey="luulposApp.payment.curency">Curency</Translate>
                  </Label>
                  <AvField id="payment-curency" type="text" name="curency" />
                </AvGroup>
                <AvGroup>
                  <Label id="customerNameLabel" for="customerName">
                    <Translate contentKey="luulposApp.payment.customerName">Customer Name</Translate>
                  </Label>
                  <AvField id="payment-customerName" type="text" name="customerName" />
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.payment.shop">Shop</Translate>
                  </Label>
                  <AvInput id="payment-shop" type="select" className="form-control" name="shopId">
                    <option value="" key="0" />
                    {shops
                      ? shops.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.shopName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="processedBy.firstName">
                    <Translate contentKey="luulposApp.payment.processedBy">Processed By</Translate>
                  </Label>
                  <AvInput id="payment-processedBy" type="select" className="form-control" name="processedById">
                    <option value="" key="0" />
                    {profiles
                      ? profiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.firstName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="paymentMethod.paymentMethod">
                    <Translate contentKey="luulposApp.payment.paymentMethod">Payment Method</Translate>
                  </Label>
                  <AvInput id="payment-paymentMethod" type="select" className="form-control" name="paymentMethodId">
                    <option value="" key="0" />
                    {paymentMethods
                      ? paymentMethods.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.paymentMethod}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="order.description">
                    <Translate contentKey="luulposApp.payment.order">Order</Translate>
                  </Label>
                  <AvInput id="payment-order" type="select" className="form-control" name="orderId">
                    <option value="" key="0" />
                    {orders
                      ? orders.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.description}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/payment" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  shops: storeState.shop.entities,
  profiles: storeState.profile.entities,
  paymentMethods: storeState.paymentMethod.entities,
  orders: storeState.orders.entities,
  paymentEntity: storeState.payment.entity,
  loading: storeState.payment.loading,
  updating: storeState.payment.updating,
  updateSuccess: storeState.payment.updateSuccess
});

const mapDispatchToProps = {
  getShops,
  getProfiles,
  getPaymentMethods,
  getOrders,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PaymentUpdate);
