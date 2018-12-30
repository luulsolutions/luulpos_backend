import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrdersLine } from 'app/shared/model/orders-line.model';
import { getEntities as getOrdersLines } from 'app/entities/orders-line/orders-line.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './orders-line-variant.reducer';
import { IOrdersLineVariant } from 'app/shared/model/orders-line-variant.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrdersLineVariantUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrdersLineVariantUpdateState {
  isNew: boolean;
  ordersLineId: string;
}

export class OrdersLineVariantUpdate extends React.Component<IOrdersLineVariantUpdateProps, IOrdersLineVariantUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      ordersLineId: '0',
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

    this.props.getOrdersLines();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { ordersLineVariantEntity } = this.props;
      const entity = {
        ...ordersLineVariantEntity,
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
    this.props.history.push('/entity/orders-line-variant');
  };

  render() {
    const { ordersLineVariantEntity, ordersLines, loading, updating } = this.props;
    const { isNew } = this.state;

    const { fullPhoto, fullPhotoContentType, thumbnailPhoto, thumbnailPhotoContentType } = ordersLineVariantEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.ordersLineVariant.home.createOrEditLabel">
              <Translate contentKey="luulposApp.ordersLineVariant.home.createOrEditLabel">Create or edit a OrdersLineVariant</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : ordersLineVariantEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="orders-line-variant-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="variantNameLabel" for="variantName">
                    <Translate contentKey="luulposApp.ordersLineVariant.variantName">Variant Name</Translate>
                  </Label>
                  <AvField id="orders-line-variant-variantName" type="text" name="variantName" />
                </AvGroup>
                <AvGroup>
                  <Label id="variantValueLabel" for="variantValue">
                    <Translate contentKey="luulposApp.ordersLineVariant.variantValue">Variant Value</Translate>
                  </Label>
                  <AvField id="orders-line-variant-variantValue" type="text" name="variantValue" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.ordersLineVariant.description">Description</Translate>
                  </Label>
                  <AvField id="orders-line-variant-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="percentageLabel" for="percentage">
                    <Translate contentKey="luulposApp.ordersLineVariant.percentage">Percentage</Translate>
                  </Label>
                  <AvField id="orders-line-variant-percentage" type="string" className="form-control" name="percentage" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="fullPhotoLabel" for="fullPhoto">
                      <Translate contentKey="luulposApp.ordersLineVariant.fullPhoto">Full Photo</Translate>
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
                    <Translate contentKey="luulposApp.ordersLineVariant.fullPhotoUrl">Full Photo Url</Translate>
                  </Label>
                  <AvField id="orders-line-variant-fullPhotoUrl" type="text" name="fullPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="thumbnailPhotoLabel" for="thumbnailPhoto">
                      <Translate contentKey="luulposApp.ordersLineVariant.thumbnailPhoto">Thumbnail Photo</Translate>
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
                    <Translate contentKey="luulposApp.ordersLineVariant.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
                  </Label>
                  <AvField id="orders-line-variant-thumbnailPhotoUrl" type="text" name="thumbnailPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="price">
                    <Translate contentKey="luulposApp.ordersLineVariant.price">Price</Translate>
                  </Label>
                  <AvField
                    id="orders-line-variant-price"
                    type="text"
                    name="price"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="ordersLine.id">
                    <Translate contentKey="luulposApp.ordersLineVariant.ordersLine">Orders Line</Translate>
                  </Label>
                  <AvInput id="orders-line-variant-ordersLine" type="select" className="form-control" name="ordersLineId">
                    <option value="" key="0" />
                    {ordersLines
                      ? ordersLines.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/orders-line-variant" replace color="info">
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
  ordersLines: storeState.ordersLine.entities,
  ordersLineVariantEntity: storeState.ordersLineVariant.entity,
  loading: storeState.ordersLineVariant.loading,
  updating: storeState.ordersLineVariant.updating,
  updateSuccess: storeState.ordersLineVariant.updateSuccess
});

const mapDispatchToProps = {
  getOrdersLines,
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
)(OrdersLineVariantUpdate);
