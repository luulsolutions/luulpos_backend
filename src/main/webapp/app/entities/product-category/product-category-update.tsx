import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './product-category.reducer';
import { IProductCategory } from 'app/shared/model/product-category.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductCategoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProductCategoryUpdateState {
  isNew: boolean;
  shopId: string;
}

export class ProductCategoryUpdate extends React.Component<IProductCategoryUpdateProps, IProductCategoryUpdateState> {
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

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { productCategoryEntity } = this.props;
      const entity = {
        ...productCategoryEntity,
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
    this.props.history.push('/entity/product-category');
  };

  render() {
    const { productCategoryEntity, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    const { imageFull, imageFullContentType, imageThumb, imageThumbContentType } = productCategoryEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.productCategory.home.createOrEditLabel">
              <Translate contentKey="luulposApp.productCategory.home.createOrEditLabel">Create or edit a ProductCategory</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : productCategoryEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="product-category-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="categoryLabel" for="category">
                    <Translate contentKey="luulposApp.productCategory.category">Category</Translate>
                  </Label>
                  <AvField id="product-category-category" type="text" name="category" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.productCategory.description">Description</Translate>
                  </Label>
                  <AvField id="product-category-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="imageFullLabel" for="imageFull">
                      <Translate contentKey="luulposApp.productCategory.imageFull">Image Full</Translate>
                    </Label>
                    <br />
                    {imageFull ? (
                      <div>
                        <a onClick={openFile(imageFullContentType, imageFull)}>
                          <img src={`data:${imageFullContentType};base64,${imageFull}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {imageFullContentType}, {byteSize(imageFull)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('imageFull')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_imageFull" type="file" onChange={this.onBlobChange(true, 'imageFull')} accept="image/*" />
                    <AvInput type="hidden" name="imageFull" value={imageFull} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="imageFullUrlLabel" for="imageFullUrl">
                    <Translate contentKey="luulposApp.productCategory.imageFullUrl">Image Full Url</Translate>
                  </Label>
                  <AvField id="product-category-imageFullUrl" type="text" name="imageFullUrl" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="imageThumbLabel" for="imageThumb">
                      <Translate contentKey="luulposApp.productCategory.imageThumb">Image Thumb</Translate>
                    </Label>
                    <br />
                    {imageThumb ? (
                      <div>
                        <a onClick={openFile(imageThumbContentType, imageThumb)}>
                          <img src={`data:${imageThumbContentType};base64,${imageThumb}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {imageThumbContentType}, {byteSize(imageThumb)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('imageThumb')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_imageThumb" type="file" onChange={this.onBlobChange(true, 'imageThumb')} accept="image/*" />
                    <AvInput type="hidden" name="imageThumb" value={imageThumb} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="imageThumbUrlLabel" for="imageThumbUrl">
                    <Translate contentKey="luulposApp.productCategory.imageThumbUrl">Image Thumb Url</Translate>
                  </Label>
                  <AvField id="product-category-imageThumbUrl" type="text" name="imageThumbUrl" />
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.productCategory.shop">Shop</Translate>
                  </Label>
                  <AvInput id="product-category-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/product-category" replace color="info">
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
  productCategoryEntity: storeState.productCategory.entity,
  loading: storeState.productCategory.loading,
  updating: storeState.productCategory.updating,
  updateSuccess: storeState.productCategory.updateSuccess
});

const mapDispatchToProps = {
  getShops,
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
)(ProductCategoryUpdate);
