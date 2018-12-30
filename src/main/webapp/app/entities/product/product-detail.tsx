import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product.reducer';
import { IProduct } from 'app/shared/model/product.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProductDetail extends React.Component<IProductDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { productEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.product.detail.title">Product</Translate> [<b>{productEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="productName">
                <Translate contentKey="luulposApp.product.productName">Product Name</Translate>
              </span>
            </dt>
            <dd>{productEntity.productName}</dd>
            <dt>
              <span id="productDescription">
                <Translate contentKey="luulposApp.product.productDescription">Product Description</Translate>
              </span>
            </dt>
            <dd>{productEntity.productDescription}</dd>
            <dt>
              <span id="price">
                <Translate contentKey="luulposApp.product.price">Price</Translate>
              </span>
            </dt>
            <dd>{productEntity.price}</dd>
            <dt>
              <span id="quantity">
                <Translate contentKey="luulposApp.product.quantity">Quantity</Translate>
              </span>
            </dt>
            <dd>{productEntity.quantity}</dd>
            <dt>
              <span id="productImageFull">
                <Translate contentKey="luulposApp.product.productImageFull">Product Image Full</Translate>
              </span>
            </dt>
            <dd>
              {productEntity.productImageFull ? (
                <div>
                  <a onClick={openFile(productEntity.productImageFullContentType, productEntity.productImageFull)}>
                    <img
                      src={`data:${productEntity.productImageFullContentType};base64,${productEntity.productImageFull}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productEntity.productImageFullContentType}, {byteSize(productEntity.productImageFull)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="productImageFullUrl">
                <Translate contentKey="luulposApp.product.productImageFullUrl">Product Image Full Url</Translate>
              </span>
            </dt>
            <dd>{productEntity.productImageFullUrl}</dd>
            <dt>
              <span id="productImageThumb">
                <Translate contentKey="luulposApp.product.productImageThumb">Product Image Thumb</Translate>
              </span>
            </dt>
            <dd>
              {productEntity.productImageThumb ? (
                <div>
                  <a onClick={openFile(productEntity.productImageThumbContentType, productEntity.productImageThumb)}>
                    <img
                      src={`data:${productEntity.productImageThumbContentType};base64,${productEntity.productImageThumb}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productEntity.productImageThumbContentType}, {byteSize(productEntity.productImageThumb)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="productImageThumbUrl">
                <Translate contentKey="luulposApp.product.productImageThumbUrl">Product Image Thumb Url</Translate>
              </span>
            </dt>
            <dd>{productEntity.productImageThumbUrl}</dd>
            <dt>
              <span id="dateCreated">
                <Translate contentKey="luulposApp.product.dateCreated">Date Created</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={productEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="barcode">
                <Translate contentKey="luulposApp.product.barcode">Barcode</Translate>
              </span>
            </dt>
            <dd>{productEntity.barcode}</dd>
            <dt>
              <span id="serialCode">
                <Translate contentKey="luulposApp.product.serialCode">Serial Code</Translate>
              </span>
            </dt>
            <dd>{productEntity.serialCode}</dd>
            <dt>
              <span id="priorityPosition">
                <Translate contentKey="luulposApp.product.priorityPosition">Priority Position</Translate>
              </span>
            </dt>
            <dd>{productEntity.priorityPosition}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="luulposApp.product.active">Active</Translate>
              </span>
            </dt>
            <dd>{productEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <span id="isVariantProduct">
                <Translate contentKey="luulposApp.product.isVariantProduct">Is Variant Product</Translate>
              </span>
            </dt>
            <dd>{productEntity.isVariantProduct ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.product.productTypes">Product Types</Translate>
            </dt>
            <dd>{productEntity.productTypesProductType ? productEntity.productTypesProductType : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.product.shop">Shop</Translate>
            </dt>
            <dd>{productEntity.shopShopName ? productEntity.shopShopName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.product.discounts">Discounts</Translate>
            </dt>
            <dd>{productEntity.discountsDescription ? productEntity.discountsDescription : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.product.taxes">Taxes</Translate>
            </dt>
            <dd>{productEntity.taxesDescription ? productEntity.taxesDescription : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.product.category">Category</Translate>
            </dt>
            <dd>{productEntity.categoryCategory ? productEntity.categoryCategory : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/product" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/product/${productEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ product }: IRootState) => ({
  productEntity: product.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductDetail);
