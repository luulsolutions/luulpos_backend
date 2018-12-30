import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-category.reducer';
import { IProductCategory } from 'app/shared/model/product-category.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductCategoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProductCategoryDetail extends React.Component<IProductCategoryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { productCategoryEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.productCategory.detail.title">ProductCategory</Translate> [<b>{productCategoryEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="category">
                <Translate contentKey="luulposApp.productCategory.category">Category</Translate>
              </span>
            </dt>
            <dd>{productCategoryEntity.category}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.productCategory.description">Description</Translate>
              </span>
            </dt>
            <dd>{productCategoryEntity.description}</dd>
            <dt>
              <span id="imageFull">
                <Translate contentKey="luulposApp.productCategory.imageFull">Image Full</Translate>
              </span>
            </dt>
            <dd>
              {productCategoryEntity.imageFull ? (
                <div>
                  <a onClick={openFile(productCategoryEntity.imageFullContentType, productCategoryEntity.imageFull)}>
                    <img
                      src={`data:${productCategoryEntity.imageFullContentType};base64,${productCategoryEntity.imageFull}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productCategoryEntity.imageFullContentType}, {byteSize(productCategoryEntity.imageFull)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="imageFullUrl">
                <Translate contentKey="luulposApp.productCategory.imageFullUrl">Image Full Url</Translate>
              </span>
            </dt>
            <dd>{productCategoryEntity.imageFullUrl}</dd>
            <dt>
              <span id="imageThumb">
                <Translate contentKey="luulposApp.productCategory.imageThumb">Image Thumb</Translate>
              </span>
            </dt>
            <dd>
              {productCategoryEntity.imageThumb ? (
                <div>
                  <a onClick={openFile(productCategoryEntity.imageThumbContentType, productCategoryEntity.imageThumb)}>
                    <img
                      src={`data:${productCategoryEntity.imageThumbContentType};base64,${productCategoryEntity.imageThumb}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productCategoryEntity.imageThumbContentType}, {byteSize(productCategoryEntity.imageThumb)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="imageThumbUrl">
                <Translate contentKey="luulposApp.productCategory.imageThumbUrl">Image Thumb Url</Translate>
              </span>
            </dt>
            <dd>{productCategoryEntity.imageThumbUrl}</dd>
            <dt>
              <Translate contentKey="luulposApp.productCategory.shop">Shop</Translate>
            </dt>
            <dd>{productCategoryEntity.shopShopName ? productCategoryEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/product-category" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/product-category/${productCategoryEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ productCategory }: IRootState) => ({
  productCategoryEntity: productCategory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductCategoryDetail);
