import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { getEntity, updateEntity, createEntity, reset } from './system-config.reducer';
import { ISystemConfig } from 'app/shared/model/system-config.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISystemConfigUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISystemConfigUpdateState {
  isNew: boolean;
  shopId: string;
}

export class SystemConfigUpdate extends React.Component<ISystemConfigUpdateProps, ISystemConfigUpdateState> {
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

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { systemConfigEntity } = this.props;
      const entity = {
        ...systemConfigEntity,
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
    this.props.history.push('/entity/system-config');
  };

  render() {
    const { systemConfigEntity, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.systemConfig.home.createOrEditLabel">
              <Translate contentKey="luulposApp.systemConfig.home.createOrEditLabel">Create or edit a SystemConfig</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : systemConfigEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="system-config-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="keyLabel" for="key">
                    <Translate contentKey="luulposApp.systemConfig.key">Key</Translate>
                  </Label>
                  <AvField id="system-config-key" type="text" name="key" />
                </AvGroup>
                <AvGroup>
                  <Label id="valueLabel" for="value">
                    <Translate contentKey="luulposApp.systemConfig.value">Value</Translate>
                  </Label>
                  <AvField id="system-config-value" type="text" name="value" />
                </AvGroup>
                <AvGroup>
                  <Label id="configurationTypeLabel">
                    <Translate contentKey="luulposApp.systemConfig.configurationType">Configuration Type</Translate>
                  </Label>
                  <AvInput
                    id="system-config-configurationType"
                    type="select"
                    className="form-control"
                    name="configurationType"
                    value={(!isNew && systemConfigEntity.configurationType) || 'STRING'}
                  >
                    <option value="STRING">
                      <Translate contentKey="luulposApp.ConfigType.STRING" />
                    </option>
                    <option value="BOOLEAN">
                      <Translate contentKey="luulposApp.ConfigType.BOOLEAN" />
                    </option>
                    <option value="NUMBER">
                      <Translate contentKey="luulposApp.ConfigType.NUMBER" />
                    </option>
                    <option value="DATE">
                      <Translate contentKey="luulposApp.ConfigType.DATE" />
                    </option>
                    <option value="FILE">
                      <Translate contentKey="luulposApp.ConfigType.FILE" />
                    </option>
                    <option value="OBJECT">
                      <Translate contentKey="luulposApp.ConfigType.OBJECT" />
                    </option>
                    <option value="ARRAY">
                      <Translate contentKey="luulposApp.ConfigType.ARRAY" />
                    </option>
                    <option value="GEO_POINT">
                      <Translate contentKey="luulposApp.ConfigType.GEO_POINT" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="noteLabel" for="note">
                    <Translate contentKey="luulposApp.systemConfig.note">Note</Translate>
                  </Label>
                  <AvField id="system-config-note" type="text" name="note" />
                </AvGroup>
                <AvGroup>
                  <Label id="enabledLabel" check>
                    <AvInput id="system-config-enabled" type="checkbox" className="form-control" name="enabled" />
                    <Translate contentKey="luulposApp.systemConfig.enabled">Enabled</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.systemConfig.shop">Shop</Translate>
                  </Label>
                  <AvInput id="system-config-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/system-config" replace color="info">
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
  systemConfigEntity: storeState.systemConfig.entity,
  loading: storeState.systemConfig.loading,
  updating: storeState.systemConfig.updating,
  updateSuccess: storeState.systemConfig.updateSuccess
});

const mapDispatchToProps = {
  getShops,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SystemConfigUpdate);
