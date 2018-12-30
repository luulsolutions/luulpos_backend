import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-variant.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductVariantDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProductVariantDetail extends React.Component<IProductVariantDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { productVariantEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.productVariant.detail.title">ProductVariant</Translate> [<b>{productVariantEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="variantName">
                <Translate contentKey="luulposApp.productVariant.variantName">Variant Name</Translate>
              </span>
            </dt>
            <dd>{productVariantEntity.variantName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.productVariant.description">Description</Translate>
              </span>
            </dt>
            <dd>{productVariantEntity.description}</dd>
            <dt>
              <span id="percentage">
                <Translate contentKey="luulposApp.productVariant.percentage">Percentage</Translate>
              </span>
            </dt>
            <dd>{productVariantEntity.percentage}</dd>
            <dt>
              <span id="fullPhoto">
                <Translate contentKey="luulposApp.productVariant.fullPhoto">Full Photo</Translate>
              </span>
            </dt>
            <dd>
              {productVariantEntity.fullPhoto ? (
                <div>
                  <a onClick={openFile(productVariantEntity.fullPhotoContentType, productVariantEntity.fullPhoto)}>
                    <img
                      src={`data:${productVariantEntity.fullPhotoContentType};base64,${productVariantEntity.fullPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productVariantEntity.fullPhotoContentType}, {byteSize(productVariantEntity.fullPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="fullPhotoUrl">
                <Translate contentKey="luulposApp.productVariant.fullPhotoUrl">Full Photo Url</Translate>
              </span>
            </dt>
            <dd>{productVariantEntity.fullPhotoUrl}</dd>
            <dt>
              <span id="thumbnailPhoto">
                <Translate contentKey="luulposApp.productVariant.thumbnailPhoto">Thumbnail Photo</Translate>
              </span>
            </dt>
            <dd>
              {productVariantEntity.thumbnailPhoto ? (
                <div>
                  <a onClick={openFile(productVariantEntity.thumbnailPhotoContentType, productVariantEntity.thumbnailPhoto)}>
                    <img
                      src={`data:${productVariantEntity.thumbnailPhotoContentType};base64,${productVariantEntity.thumbnailPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productVariantEntity.thumbnailPhotoContentType}, {byteSize(productVariantEntity.thumbnailPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="thumbnailPhotoUrl">
                <Translate contentKey="luulposApp.productVariant.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
              </span>
            </dt>
            <dd>{productVariantEntity.thumbnailPhotoUrl}</dd>
            <dt>
              <span id="price">
                <Translate contentKey="luulposApp.productVariant.price">Price</Translate>
              </span>
            </dt>
            <dd>{productVariantEntity.price}</dd>
            <dt>
              <Translate contentKey="luulposApp.productVariant.product">Product</Translate>
            </dt>
            <dd>{productVariantEntity.productProductName ? productVariantEntity.productProductName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/product-variant" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/product-variant/${productVariantEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ productVariant }: IRootState) => ({
  productVariantEntity: productVariant.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductVariantDetail);
