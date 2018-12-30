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
import { getEntity, updateEntity, createEntity, reset } from './shop-device.reducer';
import { IShopDevice } from 'app/shared/model/shop-device.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopDeviceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IShopDeviceUpdateState {
  isNew: boolean;
  shopId: string;
}

export class ShopDeviceUpdate extends React.Component<IShopDeviceUpdateProps, IShopDeviceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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

    this.props.getShops();
  }

  saveEntity = (event, errors, values) => {
    values.registeredDate = new Date(values.registeredDate);

    if (errors.length === 0) {
      const { shopDeviceEntity } = this.props;
      const entity = {
        ...shopDeviceEntity,
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
    this.props.history.push('/entity/shop-device');
  };

  render() {
    const { shopDeviceEntity, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.shopDevice.home.createOrEditLabel">
              <Translate contentKey="luulposApp.shopDevice.home.createOrEditLabel">Create or edit a ShopDevice</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : shopDeviceEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="shop-device-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="deviceNameLabel" for="deviceName">
                    <Translate contentKey="luulposApp.shopDevice.deviceName">Device Name</Translate>
                  </Label>
                  <AvField id="shop-device-deviceName" type="text" name="deviceName" />
                </AvGroup>
                <AvGroup>
                  <Label id="deviceModelLabel" for="deviceModel">
                    <Translate contentKey="luulposApp.shopDevice.deviceModel">Device Model</Translate>
                  </Label>
                  <AvField id="shop-device-deviceModel" type="text" name="deviceModel" />
                </AvGroup>
                <AvGroup>
                  <Label id="registeredDateLabel" for="registeredDate">
                    <Translate contentKey="luulposApp.shopDevice.registeredDate">Registered Date</Translate>
                  </Label>
                  <AvInput
                    id="shop-device-registeredDate"
                    type="datetime-local"
                    className="form-control"
                    name="registeredDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.shopDeviceEntity.registeredDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.shopDevice.shop">Shop</Translate>
                  </Label>
                  <AvInput id="shop-device-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/shop-device" replace color="info">
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
  shopDeviceEntity: storeState.shopDevice.entity,
  loading: storeState.shopDevice.loading,
  updating: storeState.shopDevice.updating,
  updateSuccess: storeState.shopDevice.updateSuccess
});

const mapDispatchToProps = {
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
)(ShopDeviceUpdate);
