import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './product-variant.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductVariantUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProductVariantUpdateState {
  isNew: boolean;
  productId: string;
}

export class ProductVariantUpdate extends React.Component<IProductVariantUpdateProps, IProductVariantUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
      const { productVariantEntity } = this.props;
      const entity = {
        ...productVariantEntity,
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
    this.props.history.push('/entity/product-variant');
  };

  render() {
    const { productVariantEntity, products, loading, updating } = this.props;
    const { isNew } = this.state;

    const { fullPhoto, fullPhotoContentType, thumbnailPhoto, thumbnailPhotoContentType } = productVariantEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.productVariant.home.createOrEditLabel">
              <Translate contentKey="luulposApp.productVariant.home.createOrEditLabel">Create or edit a ProductVariant</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : productVariantEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="product-variant-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="variantNameLabel" for="variantName">
                    <Translate contentKey="luulposApp.productVariant.variantName">Variant Name</Translate>
                  </Label>
                  <AvField id="product-variant-variantName" type="text" name="variantName" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.productVariant.description">Description</Translate>
                  </Label>
                  <AvField id="product-variant-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="percentageLabel" for="percentage">
                    <Translate contentKey="luulposApp.productVariant.percentage">Percentage</Translate>
                  </Label>
                  <AvField id="product-variant-percentage" type="string" className="form-control" name="percentage" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="fullPhotoLabel" for="fullPhoto">
                      <Translate contentKey="luulposApp.productVariant.fullPhoto">Full Photo</Translate>
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
                    <Translate contentKey="luulposApp.productVariant.fullPhotoUrl">Full Photo Url</Translate>
                  </Label>
                  <AvField id="product-variant-fullPhotoUrl" type="text" name="fullPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="thumbnailPhotoLabel" for="thumbnailPhoto">
                      <Translate contentKey="luulposApp.productVariant.thumbnailPhoto">Thumbnail Photo</Translate>
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
                  <Label id="thumbnailPhotoUrlLabel" for="thumbnailPhotoUrl">
                    <Translate contentKey="luulposApp.productVariant.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
                  </Label>
                  <AvField id="product-variant-thumbnailPhotoUrl" type="text" name="thumbnailPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="price">
                    <Translate contentKey="luulposApp.productVariant.price">Price</Translate>
                  </Label>
                  <AvField
                    id="product-variant-price"
                    type="text"
                    name="price"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="product.productName">
                    <Translate contentKey="luulposApp.productVariant.product">Product</Translate>
                  </Label>
                  <AvInput id="product-variant-product" type="select" className="form-control" name="productId">
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
                <Button tag={Link} id="cancel-save" to="/entity/product-variant" replace color="info">
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
  products: storeState.product.entities,
  productVariantEntity: storeState.productVariant.entity,
  loading: storeState.productVariant.loading,
  updating: storeState.productVariant.updating,
  updateSuccess: storeState.productVariant.updateSuccess
});

const mapDispatchToProps = {
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
)(ProductVariantUpdate);
