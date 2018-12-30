import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './orders-line-extra.reducer';
import { IOrdersLineExtra } from 'app/shared/model/orders-line-extra.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrdersLineExtraDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrdersLineExtraDetail extends React.Component<IOrdersLineExtraDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ordersLineExtraEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.ordersLineExtra.detail.title">OrdersLineExtra</Translate> [<b>{ordersLineExtraEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="ordersLineExtraName">
                <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraName">Orders Line Extra Name</Translate>
              </span>
            </dt>
            <dd>{ordersLineExtraEntity.ordersLineExtraName}</dd>
            <dt>
              <span id="ordersLineExtraValue">
                <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraValue">Orders Line Extra Value</Translate>
              </span>
            </dt>
            <dd>{ordersLineExtraEntity.ordersLineExtraValue}</dd>
            <dt>
              <span id="ordersLineExtraPrice">
                <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraPrice">Orders Line Extra Price</Translate>
              </span>
            </dt>
            <dd>{ordersLineExtraEntity.ordersLineExtraPrice}</dd>
            <dt>
              <span id="ordersOptionDescription">
                <Translate contentKey="luulposApp.ordersLineExtra.ordersOptionDescription">Orders Option Description</Translate>
              </span>
            </dt>
            <dd>{ordersLineExtraEntity.ordersOptionDescription}</dd>
            <dt>
              <span id="fullPhoto">
                <Translate contentKey="luulposApp.ordersLineExtra.fullPhoto">Full Photo</Translate>
              </span>
            </dt>
            <dd>
              {ordersLineExtraEntity.fullPhoto ? (
                <div>
                  <a onClick={openFile(ordersLineExtraEntity.fullPhotoContentType, ordersLineExtraEntity.fullPhoto)}>
                    <img
                      src={`data:${ordersLineExtraEntity.fullPhotoContentType};base64,${ordersLineExtraEntity.fullPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {ordersLineExtraEntity.fullPhotoContentType}, {byteSize(ordersLineExtraEntity.fullPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="fullPhotoUrl">
                <Translate contentKey="luulposApp.ordersLineExtra.fullPhotoUrl">Full Photo Url</Translate>
              </span>
            </dt>
            <dd>{ordersLineExtraEntity.fullPhotoUrl}</dd>
            <dt>
              <span id="thumbnailPhoto">
                <Translate contentKey="luulposApp.ordersLineExtra.thumbnailPhoto">Thumbnail Photo</Translate>
              </span>
            </dt>
            <dd>
              {ordersLineExtraEntity.thumbnailPhoto ? (
                <div>
                  <a onClick={openFile(ordersLineExtraEntity.thumbnailPhotoContentType, ordersLineExtraEntity.thumbnailPhoto)}>
                    <img
                      src={`data:${ordersLineExtraEntity.thumbnailPhotoContentType};base64,${ordersLineExtraEntity.thumbnailPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {ordersLineExtraEntity.thumbnailPhotoContentType}, {byteSize(ordersLineExtraEntity.thumbnailPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="thumbnailPhotoUrl">
                <Translate contentKey="luulposApp.ordersLineExtra.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
              </span>
            </dt>
            <dd>{ordersLineExtraEntity.thumbnailPhotoUrl}</dd>
            <dt>
              <Translate contentKey="luulposApp.ordersLineExtra.ordersLineVariant">Orders Line Variant</Translate>
            </dt>
            <dd>{ordersLineExtraEntity.ordersLineVariantId ? ordersLineExtraEntity.ordersLineVariantId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/orders-line-extra" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/orders-line-extra/${ordersLineExtraEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ ordersLineExtra }: IRootState) => ({
  ordersLineExtraEntity: ordersLineExtra.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrdersLineExtraDetail);
