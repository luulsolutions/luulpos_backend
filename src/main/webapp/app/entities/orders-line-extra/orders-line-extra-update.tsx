import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrdersLineVariant } from 'app/shared/model/orders-line-variant.model';
import { getEntities as getOrdersLineVariants } from 'app/entities/orders-line-variant/orders-line-variant.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './orders-line-extra.reducer';
import { IOrdersLineExtra } from 'app/shared/model/orders-line-extra.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrdersLineExtraUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrdersLineExtraUpdateState {
  isNew: boolean;
  ordersLineVariantId: string;
}

export class OrdersLineExtraUpdate extends React.Component<IOrdersLineExtraUpdateProps, IOrdersLineExtraUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      ordersLineVariantId: '0',
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

    this.props.getOrdersLineVariants();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { ordersLineExtraEntity } = this.props;
      const entity = {
        ...ordersLineExtraEntity,
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
    this.props.history.push('/entity/orders-line-extra');
  };

  render() {
    const { ordersLineExtraEntity, ordersLineVariants, loading, updating } = this.props;
    const { isNew } = this.state;

    const { fullPhoto, fullPhotoContentType, thumbnailPhoto, thumbnailPhotoContentType } = ordersLineExtraEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.ordersLineExtra.home.createOrEditLabel">
              <Translate contentKey="luulposApp.ordersLineExtra.home.createOrEditLabel">Create or edit a OrdersLineExtra</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : ordersLineExtraEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="orders-line-extra-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="ordersLineExtraNameLabel" for="ordersLineExtraName">
                    <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraName">Orders Line Extra Name</Translate>
                  </Label>
                  <AvField
                    id="orders-line-extra-ordersLineExtraName"
                    type="text"
                    name="ordersLineExtraName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="ordersLineExtraValueLabel" for="ordersLineExtraValue">
                    <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraValue">Orders Line Extra Value</Translate>
                  </Label>
                  <AvField
                    id="orders-line-extra-ordersLineExtraValue"
                    type="text"
                    name="ordersLineExtraValue"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="ordersLineExtraPriceLabel" for="ordersLineExtraPrice">
                    <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraPrice">Orders Line Extra Price</Translate>
                  </Label>
                  <AvField id="orders-line-extra-ordersLineExtraPrice" type="string" className="form-control" name="ordersLineExtraPrice" />
                </AvGroup>
                <AvGroup>
                  <Label id="ordersOptionDescriptionLabel" for="ordersOptionDescription">
                    <Translate contentKey="luulposApp.ordersLineExtra.ordersOptionDescription">Orders Option Description</Translate>
                  </Label>
                  <AvField id="orders-line-extra-ordersOptionDescription" type="text" name="ordersOptionDescription" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="fullPhotoLabel" for="fullPhoto">
                      <Translate contentKey="luulposApp.ordersLineExtra.fullPhoto">Full Photo</Translate>
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
                    <Translate contentKey="luulposApp.ordersLineExtra.fullPhotoUrl">Full Photo Url</Translate>
                  </Label>
                  <AvField id="orders-line-extra-fullPhotoUrl" type="text" name="fullPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="thumbnailPhotoLabel" for="thumbnailPhoto">
                      <Translate contentKey="luulposApp.ordersLineExtra.thumbnailPhoto">Thumbnail Photo</Translate>
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
                    <Translate contentKey="luulposApp.ordersLineExtra.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
                  </Label>
                  <AvField id="orders-line-extra-thumbnailPhotoUrl" type="text" name="thumbnailPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label for="ordersLineVariant.id">
                    <Translate contentKey="luulposApp.ordersLineExtra.ordersLineVariant">Orders Line Variant</Translate>
                  </Label>
                  <AvInput id="orders-line-extra-ordersLineVariant" type="select" className="form-control" name="ordersLineVariantId">
                    <option value="" key="0" />
                    {ordersLineVariants
                      ? ordersLineVariants.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/orders-line-extra" replace color="info">
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
  ordersLineVariants: storeState.ordersLineVariant.entities,
  ordersLineExtraEntity: storeState.ordersLineExtra.entity,
  loading: storeState.ordersLineExtra.loading,
  updating: storeState.ordersLineExtra.updating,
  updateSuccess: storeState.ordersLineExtra.updateSuccess
});

const mapDispatchToProps = {
  getOrdersLineVariants,
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
)(OrdersLineExtraUpdate);
