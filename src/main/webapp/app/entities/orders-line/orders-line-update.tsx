import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrders } from 'app/shared/model/orders.model';
import { getEntities as getOrders } from 'app/entities/orders/orders.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './orders-line.reducer';
import { IOrdersLine } from 'app/shared/model/orders-line.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrdersLineUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrdersLineUpdateState {
  isNew: boolean;
  ordersId: string;
  productId: string;
}

export class OrdersLineUpdate extends React.Component<IOrdersLineUpdateProps, IOrdersLineUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      ordersId: '0',
      productId: '0',
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

    this.props.getOrders();
    this.props.getProducts();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { ordersLineEntity } = this.props;
      const entity = {
        ...ordersLineEntity,
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
    this.props.history.push('/entity/orders-line');
  };

  render() {
    const { ordersLineEntity, orders, products, loading, updating } = this.props;
    const { isNew } = this.state;

    const { thumbnailPhoto, thumbnailPhotoContentType, fullPhoto, fullPhotoContentType } = ordersLineEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.ordersLine.home.createOrEditLabel">
              <Translate contentKey="luulposApp.ordersLine.home.createOrEditLabel">Create or edit a OrdersLine</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : ordersLineEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="orders-line-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="ordersLineNameLabel" for="ordersLineName">
                    <Translate contentKey="luulposApp.ordersLine.ordersLineName">Orders Line Name</Translate>
                  </Label>
                  <AvField
                    id="orders-line-ordersLineName"
                    type="text"
                    name="ordersLineName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="ordersLineValueLabel" for="ordersLineValue">
                    <Translate contentKey="luulposApp.ordersLine.ordersLineValue">Orders Line Value</Translate>
                  </Label>
                  <AvField
                    id="orders-line-ordersLineValue"
                    type="text"
                    name="ordersLineValue"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="ordersLinePriceLabel" for="ordersLinePrice">
                    <Translate contentKey="luulposApp.ordersLine.ordersLinePrice">Orders Line Price</Translate>
                  </Label>
                  <AvField id="orders-line-ordersLinePrice" type="string" className="form-control" name="ordersLinePrice" />
                </AvGroup>
                <AvGroup>
                  <Label id="ordersLineDescriptionLabel" for="ordersLineDescription">
                    <Translate contentKey="luulposApp.ordersLine.ordersLineDescription">Orders Line Description</Translate>
                  </Label>
                  <AvField id="orders-line-ordersLineDescription" type="text" name="ordersLineDescription" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="thumbnailPhotoLabel" for="thumbnailPhoto">
                      <Translate contentKey="luulposApp.ordersLine.thumbnailPhoto">Thumbnail Photo</Translate>
                    </Label>
                    <br />
                    {thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(thumbnailPhotoContentType, thumbnailPhoto)}>
                          <img src={`data:${thumbnailPhotoContentType};base64,${thumbnailPhoto}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {thumbnailPhotoContentType}, {byteSize(thumbnailPhoto)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('thumbnailPhoto')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_thumbnailPhoto" type="file" onChange={this.onBlobChange(true, 'thumbnailPhoto')} accept="image/*" />
                    <AvInput type="hidden" name="thumbnailPhoto" value={thumbnailPhoto} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="fullPhotoLabel" for="fullPhoto">
                      <Translate contentKey="luulposApp.ordersLine.fullPhoto">Full Photo</Translate>
                    </Label>
                    <br />
                    {fullPhoto ? (
                      <div>
                        <a onClick={openFile(fullPhotoContentType, fullPhoto)}>
                          <img src={`data:${fullPhotoContentType};base64,${fullPhoto}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {fullPhotoContentType}, {byteSize(fullPhoto)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('fullPhoto')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_fullPhoto" type="file" onChange={this.onBlobChange(true, 'fullPhoto')} accept="image/*" />
                    <AvInput type="hidden" name="fullPhoto" value={fullPhoto} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="fullPhotoUrlLabel" for="fullPhotoUrl">
                    <Translate contentKey="luulposApp.ordersLine.fullPhotoUrl">Full Photo Url</Translate>
                  </Label>
                  <AvField id="orders-line-fullPhotoUrl" type="text" name="fullPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="thumbnailPhotoUrlLabel" for="thumbnailPhotoUrl">
                    <Translate contentKey="luulposApp.ordersLine.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
                  </Label>
                  <AvField id="orders-line-thumbnailPhotoUrl" type="text" name="thumbnailPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label for="orders.id">
                    <Translate contentKey="luulposApp.ordersLine.orders">Orders</Translate>
                  </Label>
                  <AvInput id="orders-line-orders" type="select" className="form-control" name="ordersId">
                    <option value="" key="0" />
                    {orders
                      ? orders.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="product.productName">
                    <Translate contentKey="luulposApp.ordersLine.product">Product</Translate>
                  </Label>
                  <AvInput id="orders-line-product" type="select" className="form-control" name="productId">
                    <option value="" key="0" />
                    {products
                      ? products.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.productName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/orders-line" replace color="info">
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
  orders: storeState.orders.entities,
  products: storeState.product.entities,
  ordersLineEntity: storeState.ordersLine.entity,
  loading: storeState.ordersLine.loading,
  updating: storeState.ordersLine.updating,
  updateSuccess: storeState.ordersLine.updateSuccess
});

const mapDispatchToProps = {
  getOrders,
  getProducts,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrdersLineUpdate);
