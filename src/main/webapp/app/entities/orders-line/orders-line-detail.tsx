import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './orders-line.reducer';
import { IOrdersLine } from 'app/shared/model/orders-line.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrdersLineDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrdersLineDetail extends React.Component<IOrdersLineDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ordersLineEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.ordersLine.detail.title">OrdersLine</Translate> [<b>{ordersLineEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="ordersLineName">
                <Translate contentKey="luulposApp.ordersLine.ordersLineName">Orders Line Name</Translate>
              </span>
            </dt>
            <dd>{ordersLineEntity.ordersLineName}</dd>
            <dt>
              <span id="ordersLineValue">
                <Translate contentKey="luulposApp.ordersLine.ordersLineValue">Orders Line Value</Translate>
              </span>
            </dt>
            <dd>{ordersLineEntity.ordersLineValue}</dd>
            <dt>
              <span id="ordersLinePrice">
                <Translate contentKey="luulposApp.ordersLine.ordersLinePrice">Orders Line Price</Translate>
              </span>
            </dt>
            <dd>{ordersLineEntity.ordersLinePrice}</dd>
            <dt>
              <span id="ordersLineDescription">
                <Translate contentKey="luulposApp.ordersLine.ordersLineDescription">Orders Line Description</Translate>
              </span>
            </dt>
            <dd>{ordersLineEntity.ordersLineDescription}</dd>
            <dt>
              <span id="thumbnailPhoto">
                <Translate contentKey="luulposApp.ordersLine.thumbnailPhoto">Thumbnail Photo</Translate>
              </span>
            </dt>
            <dd>
              {ordersLineEntity.thumbnailPhoto ? (
                <div>
                  <a onClick={openFile(ordersLineEntity.thumbnailPhotoContentType, ordersLineEntity.thumbnailPhoto)}>
                    <img
                      src={`data:${ordersLineEntity.thumbnailPhotoContentType};base64,${ordersLineEntity.thumbnailPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {ordersLineEntity.thumbnailPhotoContentType}, {byteSize(ordersLineEntity.thumbnailPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="fullPhoto">
                <Translate contentKey="luulposApp.ordersLine.fullPhoto">Full Photo</Translate>
              </span>
            </dt>
            <dd>
              {ordersLineEntity.fullPhoto ? (
                <div>
                  <a onClick={openFile(ordersLineEntity.fullPhotoContentType, ordersLineEntity.fullPhoto)}>
                    <img
                      src={`data:${ordersLineEntity.fullPhotoContentType};base64,${ordersLineEntity.fullPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {ordersLineEntity.fullPhotoContentType}, {byteSize(ordersLineEntity.fullPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="fullPhotoUrl">
                <Translate contentKey="luulposApp.ordersLine.fullPhotoUrl">Full Photo Url</Translate>
              </span>
            </dt>
            <dd>{ordersLineEntity.fullPhotoUrl}</dd>
            <dt>
              <span id="thumbnailPhotoUrl">
                <Translate contentKey="luulposApp.ordersLine.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
              </span>
            </dt>
            <dd>{ordersLineEntity.thumbnailPhotoUrl}</dd>
            <dt>
              <Translate contentKey="luulposApp.ordersLine.orders">Orders</Translate>
            </dt>
            <dd>{ordersLineEntity.ordersId ? ordersLineEntity.ordersId : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.ordersLine.product">Product</Translate>
            </dt>
            <dd>{ordersLineEntity.productProductName ? ordersLineEntity.productProductName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/orders-line" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/orders-line/${ordersLineEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ ordersLine }: IRootState) => ({
  ordersLineEntity: ordersLine.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrdersLineDetail);
