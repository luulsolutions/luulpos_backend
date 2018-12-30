import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-device.reducer';
import { IShopDevice } from 'app/shared/model/shop-device.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopDeviceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShopDeviceDetail extends React.Component<IShopDeviceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shopDeviceEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.shopDevice.detail.title">ShopDevice</Translate> [<b>{shopDeviceEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="deviceName">
                <Translate contentKey="luulposApp.shopDevice.deviceName">Device Name</Translate>
              </span>
            </dt>
            <dd>{shopDeviceEntity.deviceName}</dd>
            <dt>
              <span id="deviceModel">
                <Translate contentKey="luulposApp.shopDevice.deviceModel">Device Model</Translate>
              </span>
            </dt>
            <dd>{shopDeviceEntity.deviceModel}</dd>
            <dt>
              <span id="registeredDate">
                <Translate contentKey="luulposApp.shopDevice.registeredDate">Registered Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={shopDeviceEntity.registeredDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="luulposApp.shopDevice.shop">Shop</Translate>
            </dt>
            <dd>{shopDeviceEntity.shopShopName ? shopDeviceEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/shop-device" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/shop-device/${shopDeviceEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ shopDevice }: IRootState) => ({
  shopDeviceEntity: shopDevice.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShopDeviceDetail);
