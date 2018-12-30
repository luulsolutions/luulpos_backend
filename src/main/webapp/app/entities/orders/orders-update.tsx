import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { getEntities as getPaymentMethods } from 'app/entities/payment-method/payment-method.reducer';
import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { IShopDevice } from 'app/shared/model/shop-device.model';
import { getEntities as getShopDevices } from 'app/entities/shop-device/shop-device.reducer';
import { ISectionTable } from 'app/shared/model/section-table.model';
import { getEntities as getSectionTables } from 'app/entities/section-table/section-table.reducer';
import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { getEntity, updateEntity, createEntity, reset } from './orders.reducer';
import { IOrders } from 'app/shared/model/orders.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrdersUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrdersUpdateState {
  isNew: boolean;
  paymentMethodId: string;
  soldById: string;
  preparedById: string;
  shopDeviceId: string;
  sectionTableId: string;
  shopId: string;
}

export class OrdersUpdate extends React.Component<IOrdersUpdateProps, IOrdersUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      paymentMethodId: '0',
      soldById: '0',
      preparedById: '0',
      shopDeviceId: '0',
      sectionTableId: '0',
      shopId: '0',
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

    this.props.getPaymentMethods();
    this.props.getProfiles();
    this.props.getShopDevices();
    this.props.getSectionTables();
    this.props.getShops();
  }

  saveEntity = (event, errors, values) => {
    values.orderDate = new Date(values.orderDate);

    if (errors.length === 0) {
      const { ordersEntity } = this.props;
      const entity = {
        ...ordersEntity,
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
    this.props.history.push('/entity/orders');
  };

  render() {
    const { ordersEntity, paymentMethods, profiles, shopDevices, sectionTables, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.orders.home.createOrEditLabel">
              <Translate contentKey="luulposApp.orders.home.createOrEditLabel">Create or edit a Orders</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : ordersEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="orders-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.orders.description">Description</Translate>
                  </Label>
                  <AvField id="orders-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="customerNameLabel" for="customerName">
                    <Translate contentKey="luulposApp.orders.customerName">Customer Name</Translate>
                  </Label>
                  <AvField id="orders-customerName" type="text" name="customerName" />
                </AvGroup>
                <AvGroup>
                  <Label id="totalPriceLabel" for="totalPrice">
                    <Translate contentKey="luulposApp.orders.totalPrice">Total Price</Translate>
                  </Label>
                  <AvField
                    id="orders-totalPrice"
                    type="text"
                    name="totalPrice"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="quantityLabel" for="quantity">
                    <Translate contentKey="luulposApp.orders.quantity">Quantity</Translate>
                  </Label>
                  <AvField
                    id="orders-quantity"
                    type="string"
                    className="form-control"
                    name="quantity"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="discountPercentageLabel" for="discountPercentage">
                    <Translate contentKey="luulposApp.orders.discountPercentage">Discount Percentage</Translate>
                  </Label>
                  <AvField id="orders-discountPercentage" type="string" className="form-control" name="discountPercentage" />
                </AvGroup>
                <AvGroup>
                  <Label id="discountAmountLabel" for="discountAmount">
                    <Translate contentKey="luulposApp.orders.discountAmount">Discount Amount</Translate>
                  </Label>
                  <AvField id="orders-discountAmount" type="text" name="discountAmount" />
                </AvGroup>
                <AvGroup>
                  <Label id="taxPercentageLabel" for="taxPercentage">
                    <Translate contentKey="luulposApp.orders.taxPercentage">Tax Percentage</Translate>
                  </Label>
                  <AvField id="orders-taxPercentage" type="string" className="form-control" name="taxPercentage" />
                </AvGroup>
                <AvGroup>
                  <Label id="taxAmountLabel" for="taxAmount">
                    <Translate contentKey="luulposApp.orders.taxAmount">Tax Amount</Translate>
                  </Label>
                  <AvField id="orders-taxAmount" type="text" name="taxAmount" />
                </AvGroup>
                <AvGroup>
                  <Label id="orderStatusLabel">
                    <Translate contentKey="luulposApp.orders.orderStatus">Order Status</Translate>
                  </Label>
                  <AvInput
                    id="orders-orderStatus"
                    type="select"
                    className="form-control"
                    name="orderStatus"
                    value={(!isNew && ordersEntity.orderStatus) || 'INCOMPLETE'}
                  >
                    <option value="INCOMPLETE">
                      <Translate contentKey="luulposApp.OrderStatus.INCOMPLETE" />
                    </option>
                    <option value="COMPLETED">
                      <Translate contentKey="luulposApp.OrderStatus.COMPLETED" />
                    </option>
                    <option value="PENDING">
                      <Translate contentKey="luulposApp.OrderStatus.PENDING" />
                    </option>
                    <option value="READY">
                      <Translate contentKey="luulposApp.OrderStatus.READY" />
                    </option>
                    <option value="CANCELLED">
                      <Translate contentKey="luulposApp.OrderStatus.CANCELLED" />
                    </option>
                    <option value="REFUNDED">
                      <Translate contentKey="luulposApp.OrderStatus.REFUNDED" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="noteLabel" for="note">
                    <Translate contentKey="luulposApp.orders.note">Note</Translate>
                  </Label>
                  <AvField id="orders-note" type="text" name="note" />
                </AvGroup>
                <AvGroup>
                  <Label id="orderDateLabel" for="orderDate">
                    <Translate contentKey="luulposApp.orders.orderDate">Order Date</Translate>
                  </Label>
                  <AvInput
                    id="orders-orderDate"
                    type="datetime-local"
                    className="form-control"
                    name="orderDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.ordersEntity.orderDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="isVariantOrderLabel" check>
                    <AvInput id="orders-isVariantOrder" type="checkbox" className="form-control" name="isVariantOrder" />
                    <Translate contentKey="luulposApp.orders.isVariantOrder">Is Variant Order</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="paymentMethod.paymentMethod">
                    <Translate contentKey="luulposApp.orders.paymentMethod">Payment Method</Translate>
                  </Label>
                  <AvInput id="orders-paymentMethod" type="select" className="form-control" name="paymentMethodId">
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
                  <Label for="soldBy.firstName">
                    <Translate contentKey="luulposApp.orders.soldBy">Sold By</Translate>
                  </Label>
                  <AvInput id="orders-soldBy" type="select" className="form-control" name="soldById">
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
                  <Label for="preparedBy.firstName">
                    <Translate contentKey="luulposApp.orders.preparedBy">Prepared By</Translate>
                  </Label>
                  <AvInput id="orders-preparedBy" type="select" className="form-control" name="preparedById">
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
                  <Label for="shopDevice.deviceName">
                    <Translate contentKey="luulposApp.orders.shopDevice">Shop Device</Translate>
                  </Label>
                  <AvInput id="orders-shopDevice" type="select" className="form-control" name="shopDeviceId">
                    <option value="" key="0" />
                    {shopDevices
                      ? shopDevices.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.deviceName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="sectionTable.tableNumber">
                    <Translate contentKey="luulposApp.orders.sectionTable">Section Table</Translate>
                  </Label>
                  <AvInput id="orders-sectionTable" type="select" className="form-control" name="sectionTableId">
                    <option value="" key="0" />
                    {sectionTables
                      ? sectionTables.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.tableNumber}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.orders.shop">Shop</Translate>
                  </Label>
                  <AvInput id="orders-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/orders" replace color="info">
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
  paymentMethods: storeState.paymentMethod.entities,
  profiles: storeState.profile.entities,
  shopDevices: storeState.shopDevice.entities,
  sectionTables: storeState.sectionTable.entities,
  shops: storeState.shop.entities,
  ordersEntity: storeState.orders.entity,
  loading: storeState.orders.loading,
  updating: storeState.orders.updating,
  updateSuccess: storeState.orders.updateSuccess
});

const mapDispatchToProps = {
  getPaymentMethods,
  getProfiles,
  getShopDevices,
  getSectionTables,
  getShops,
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
)(OrdersUpdate);
