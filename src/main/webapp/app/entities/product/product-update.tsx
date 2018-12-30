import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProductType } from 'app/shared/model/product-type.model';
import { getEntities as getProductTypes } from 'app/entities/product-type/product-type.reducer';
import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { IDiscount } from 'app/shared/model/discount.model';
import { getEntities as getDiscounts } from 'app/entities/discount/discount.reducer';
import { ITax } from 'app/shared/model/tax.model';
import { getEntities as getTaxes } from 'app/entities/tax/tax.reducer';
import { IProductCategory } from 'app/shared/model/product-category.model';
import { getEntities as getProductCategories } from 'app/entities/product-category/product-category.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './product.reducer';
import { IProduct } from 'app/shared/model/product.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProductUpdateState {
  isNew: boolean;
  productTypesId: string;
  shopId: string;
  discountsId: string;
  taxesId: string;
  categoryId: string;
}

export class ProductUpdate extends React.Component<IProductUpdateProps, IProductUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      productTypesId: '0',
      shopId: '0',
      discountsId: '0',
      taxesId: '0',
      categoryId: '0',
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

    this.props.getProductTypes();
    this.props.getShops();
    this.props.getDiscounts();
    this.props.getTaxes();
    this.props.getProductCategories();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.dateCreated = new Date(values.dateCreated);

    if (errors.length === 0) {
      const { productEntity } = this.props;
      const entity = {
        ...productEntity,
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
    this.props.history.push('/entity/product');
  };

  render() {
    const { productEntity, productTypes, shops, discounts, taxes, productCategories, loading, updating } = this.props;
    const { isNew } = this.state;

    const { productImageFull, productImageFullContentType, productImageThumb, productImageThumbContentType } = productEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.product.home.createOrEditLabel">
              <Translate contentKey="luulposApp.product.home.createOrEditLabel">Create or edit a Product</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : productEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="product-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="productNameLabel" for="productName">
                    <Translate contentKey="luulposApp.product.productName">Product Name</Translate>
                  </Label>
                  <AvField
                    id="product-productName"
                    type="text"
                    name="productName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 30, errorMessage: translate('entity.validation.maxlength', { max: 30 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="productDescriptionLabel" for="productDescription">
                    <Translate contentKey="luulposApp.product.productDescription">Product Description</Translate>
                  </Label>
                  <AvField
                    id="product-productDescription"
                    type="text"
                    name="productDescription"
                    validate={{
                      maxLength: { value: 99, errorMessage: translate('entity.validation.maxlength', { max: 99 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="price">
                    <Translate contentKey="luulposApp.product.price">Price</Translate>
                  </Label>
                  <AvField id="product-price" type="text" name="price" />
                </AvGroup>
                <AvGroup>
                  <Label id="quantityLabel" for="quantity">
                    <Translate contentKey="luulposApp.product.quantity">Quantity</Translate>
                  </Label>
                  <AvField
                    id="product-quantity"
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
                  <AvGroup>
                    <Label id="productImageFullLabel" for="productImageFull">
                      <Translate contentKey="luulposApp.product.productImageFull">Product Image Full</Translate>
                    </Label>
                    <br />
                    {productImageFull ? (
                      <div>
                        <a onClick={openFile(productImageFullContentType, productImageFull)}>
                          <img src={`data:${productImageFullContentType};base64,${productImageFull}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {productImageFullContentType}, {byteSize(productImageFull)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('productImageFull')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_productImageFull" type="file" onChange={this.onBlobChange(true, 'productImageFull')} accept="image/*" />
                    <AvInput type="hidden" name="productImageFull" value={productImageFull} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="productImageFullUrlLabel" for="productImageFullUrl">
                    <Translate contentKey="luulposApp.product.productImageFullUrl">Product Image Full Url</Translate>
                  </Label>
                  <AvField id="product-productImageFullUrl" type="text" name="productImageFullUrl" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="productImageThumbLabel" for="productImageThumb">
                      <Translate contentKey="luulposApp.product.productImageThumb">Product Image Thumb</Translate>
                    </Label>
                    <br />
                    {productImageThumb ? (
                      <div>
                        <a onClick={openFile(productImageThumbContentType, productImageThumb)}>
                          <img src={`data:${productImageThumbContentType};base64,${productImageThumb}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {productImageThumbContentType}, {byteSize(productImageThumb)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('productImageThumb')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input
                      id="file_productImageThumb"
                      type="file"
                      onChange={this.onBlobChange(true, 'productImageThumb')}
                      accept="image/*"
                    />
                    <AvInput type="hidden" name="productImageThumb" value={productImageThumb} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="productImageThumbUrlLabel" for="productImageThumbUrl">
                    <Translate contentKey="luulposApp.product.productImageThumbUrl">Product Image Thumb Url</Translate>
                  </Label>
                  <AvField id="product-productImageThumbUrl" type="text" name="productImageThumbUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="dateCreatedLabel" for="dateCreated">
                    <Translate contentKey="luulposApp.product.dateCreated">Date Created</Translate>
                  </Label>
                  <AvInput
                    id="product-dateCreated"
                    type="datetime-local"
                    className="form-control"
                    name="dateCreated"
                    value={isNew ? null : convertDateTimeFromServer(this.props.productEntity.dateCreated)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="barcodeLabel" for="barcode">
                    <Translate contentKey="luulposApp.product.barcode">Barcode</Translate>
                  </Label>
                  <AvField id="product-barcode" type="text" name="barcode" />
                </AvGroup>
                <AvGroup>
                  <Label id="serialCodeLabel" for="serialCode">
                    <Translate contentKey="luulposApp.product.serialCode">Serial Code</Translate>
                  </Label>
                  <AvField id="product-serialCode" type="text" name="serialCode" />
                </AvGroup>
                <AvGroup>
                  <Label id="priorityPositionLabel" for="priorityPosition">
                    <Translate contentKey="luulposApp.product.priorityPosition">Priority Position</Translate>
                  </Label>
                  <AvField id="product-priorityPosition" type="string" className="form-control" name="priorityPosition" />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="product-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="luulposApp.product.active">Active</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="isVariantProductLabel" check>
                    <AvInput id="product-isVariantProduct" type="checkbox" className="form-control" name="isVariantProduct" />
                    <Translate contentKey="luulposApp.product.isVariantProduct">Is Variant Product</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="productTypes.productType">
                    <Translate contentKey="luulposApp.product.productTypes">Product Types</Translate>
                  </Label>
                  <AvInput id="product-productTypes" type="select" className="form-control" name="productTypesId">
                    <option value="" key="0" />
                    {productTypes
                      ? productTypes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.productType}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.product.shop">Shop</Translate>
                  </Label>
                  <AvInput id="product-shop" type="select" className="form-control" name="shopId">
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
                <AvGroup>
                  <Label for="discounts.description">
                    <Translate contentKey="luulposApp.product.discounts">Discounts</Translate>
                  </Label>
                  <AvInput id="product-discounts" type="select" className="form-control" name="discountsId">
                    <option value="" key="0" />
                    {discounts
                      ? discounts.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.description}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="taxes.description">
                    <Translate contentKey="luulposApp.product.taxes">Taxes</Translate>
                  </Label>
                  <AvInput id="product-taxes" type="select" className="form-control" name="taxesId">
                    <option value="" key="0" />
                    {taxes
                      ? taxes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.description}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="category.category">
                    <Translate contentKey="luulposApp.product.category">Category</Translate>
                  </Label>
                  <AvInput id="product-category" type="select" className="form-control" name="categoryId">
                    <option value="" key="0" />
                    {productCategories
                      ? productCategories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.category}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/product" replace color="info">
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
  productTypes: storeState.productType.entities,
  shops: storeState.shop.entities,
  discounts: storeState.discount.entities,
  taxes: storeState.tax.entities,
  productCategories: storeState.productCategory.entities,
  productEntity: storeState.product.entity,
  loading: storeState.product.loading,
  updating: storeState.product.updating,
  updateSuccess: storeState.product.updateSuccess
});

const mapDispatchToProps = {
  getProductTypes,
  getShops,
  getDiscounts,
  getTaxes,
  getProductCategories,
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
)(ProductUpdate);
