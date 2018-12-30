import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShopSection } from 'app/shared/model/shop-section.model';
import { getEntities as getShopSections } from 'app/entities/shop-section/shop-section.reducer';
import { getEntity, updateEntity, createEntity, reset } from './section-table.reducer';
import { ISectionTable } from 'app/shared/model/section-table.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISectionTableUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISectionTableUpdateState {
  isNew: boolean;
  shopSectionsId: string;
}

export class SectionTableUpdate extends React.Component<ISectionTableUpdateProps, ISectionTableUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      shopSectionsId: '0',
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

    this.props.getShopSections();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { sectionTableEntity } = this.props;
      const entity = {
        ...sectionTableEntity,
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
    this.props.history.push('/entity/section-table');
  };

  render() {
    const { sectionTableEntity, shopSections, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.sectionTable.home.createOrEditLabel">
              <Translate contentKey="luulposApp.sectionTable.home.createOrEditLabel">Create or edit a SectionTable</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : sectionTableEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="section-table-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="tableNumberLabel" for="tableNumber">
                    <Translate contentKey="luulposApp.sectionTable.tableNumber">Table Number</Translate>
                  </Label>
                  <AvField id="section-table-tableNumber" type="string" className="form-control" name="tableNumber" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.sectionTable.description">Description</Translate>
                  </Label>
                  <AvField id="section-table-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label for="shopSections.sectionName">
                    <Translate contentKey="luulposApp.sectionTable.shopSections">Shop Sections</Translate>
                  </Label>
                  <AvInput id="section-table-shopSections" type="select" className="form-control" name="shopSectionsId">
                    <option value="" key="0" />
                    {shopSections
                      ? shopSections.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.sectionName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/section-table" replace color="info">
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
  shopSections: storeState.shopSection.entities,
  sectionTableEntity: storeState.sectionTable.entity,
  loading: storeState.sectionTable.loading,
  updating: storeState.sectionTable.updating,
  updateSuccess: storeState.sectionTable.updateSuccess
});

const mapDispatchToProps = {
  getShopSections,
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
)(SectionTableUpdate);
