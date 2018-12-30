import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './orders-line-variant.reducer';
import { IOrdersLineVariant } from 'app/shared/model/orders-line-variant.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrdersLineVariantDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrdersLineVariantDetail extends React.Component<IOrdersLineVariantDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ordersLineVariantEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.ordersLineVariant.detail.title">OrdersLineVariant</Translate> [<b>
              {ordersLineVariantEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="variantName">
                <Translate contentKey="luulposApp.ordersLineVariant.variantName">Variant Name</Translate>
              </span>
            </dt>
            <dd>{ordersLineVariantEntity.variantName}</dd>
            <dt>
              <span id="variantValue">
                <Translate contentKey="luulposApp.ordersLineVariant.variantValue">Variant Value</Translate>
              </span>
            </dt>
            <dd>{ordersLineVariantEntity.variantValue}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.ordersLineVariant.description">Description</Translate>
              </span>
            </dt>
            <dd>{ordersLineVariantEntity.description}</dd>
            <dt>
              <span id="percentage">
                <Translate contentKey="luulposApp.ordersLineVariant.percentage">Percentage</Translate>
              </span>
            </dt>
            <dd>{ordersLineVariantEntity.percentage}</dd>
            <dt>
              <span id="fullPhoto">
                <Translate contentKey="luulposApp.ordersLineVariant.fullPhoto">Full Photo</Translate>
              </span>
            </dt>
            <dd>
              {ordersLineVariantEntity.fullPhoto ? (
                <div>
                  <a onClick={openFile(ordersLineVariantEntity.fullPhotoContentType, ordersLineVariantEntity.fullPhoto)}>
                    <img
                      src={`data:${ordersLineVariantEntity.fullPhotoContentType};base64,${ordersLineVariantEntity.fullPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {ordersLineVariantEntity.fullPhotoContentType}, {byteSize(ordersLineVariantEntity.fullPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="fullPhotoUrl">
                <Translate contentKey="luulposApp.ordersLineVariant.fullPhotoUrl">Full Photo Url</Translate>
              </span>
            </dt>
            <dd>{ordersLineVariantEntity.fullPhotoUrl}</dd>
            <dt>
              <span id="thumbnailPhoto">
                <Translate contentKey="luulposApp.ordersLineVariant.thumbnailPhoto">Thumbnail Photo</Translate>
              </span>
            </dt>
            <dd>
              {ordersLineVariantEntity.thumbnailPhoto ? (
                <div>
                  <a onClick={openFile(ordersLineVariantEntity.thumbnailPhotoContentType, ordersLineVariantEntity.thumbnailPhoto)}>
                    <img
                      src={`data:${ordersLineVariantEntity.thumbnailPhotoContentType};base64,${ordersLineVariantEntity.thumbnailPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {ordersLineVariantEntity.thumbnailPhotoContentType}, {byteSize(ordersLineVariantEntity.thumbnailPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="thumbnailPhotoUrl">
                <Translate contentKey="luulposApp.ordersLineVariant.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
              </span>
            </dt>
            <dd>{ordersLineVariantEntity.thumbnailPhotoUrl}</dd>
            <dt>
              <span id="price">
                <Translate contentKey="luulposApp.ordersLineVariant.price">Price</Translate>
              </span>
            </dt>
            <dd>{ordersLineVariantEntity.price}</dd>
            <dt>
              <Translate contentKey="luulposApp.ordersLineVariant.ordersLine">Orders Line</Translate>
            </dt>
            <dd>{ordersLineVariantEntity.ordersLineId ? ordersLineVariantEntity.ordersLineId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/orders-line-variant" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/orders-line-variant/${ordersLineVariantEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ ordersLineVariant }: IRootState) => ({
  ordersLineVariantEntity: ordersLineVariant.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrdersLineVariantDetail);
