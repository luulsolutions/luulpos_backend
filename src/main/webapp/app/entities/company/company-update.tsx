import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './company.reducer';
import { ICompany } from 'app/shared/model/company.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICompanyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICompanyUpdateState {
  isNew: boolean;
}

export class CompanyUpdate extends React.Component<ICompanyUpdateProps, ICompanyUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { companyEntity } = this.props;
      const entity = {
        ...companyEntity,
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
    this.props.history.push('/entity/company');
  };

  render() {
    const { companyEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    const { companyLogo, companyLogoContentType } = companyEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.company.home.createOrEditLabel">
              <Translate contentKey="luulposApp.company.home.createOrEditLabel">Create or edit a Company</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : companyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="company-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="companyNameLabel" for="companyName">
                    <Translate contentKey="luulposApp.company.companyName">Company Name</Translate>
                  </Label>
                  <AvField
                    id="company-companyName"
                    type="text"
                    name="companyName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.company.description">Description</Translate>
                  </Label>
                  <AvField id="company-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="noteLabel" for="note">
                    <Translate contentKey="luulposApp.company.note">Note</Translate>
                  </Label>
                  <AvField id="company-note" type="text" name="note" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="companyLogoLabel" for="companyLogo">
                      <Translate contentKey="luulposApp.company.companyLogo">Company Logo</Translate>
                    </Label>
                    <br />
                    {companyLogo ? (
                      <div>
                        <a onClick={openFile(companyLogoContentType, companyLogo)}>
                          <img src={`data:${companyLogoContentType};base64,${companyLogo}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {companyLogoContentType}, {byteSize(companyLogo)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('companyLogo')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_companyLogo" type="file" onChange={this.onBlobChange(true, 'companyLogo')} accept="image/*" />
                    <AvInput type="hidden" name="companyLogo" value={companyLogo} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="companyLogoUrlLabel" for="companyLogoUrl">
                    <Translate contentKey="luulposApp.company.companyLogoUrl">Company Logo Url</Translate>
                  </Label>
                  <AvField id="company-companyLogoUrl" type="text" name="companyLogoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="company-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="luulposApp.company.active">Active</Translate>
                  </Label>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/company" replace color="info">
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
  companyEntity: storeState.company.entity,
  loading: storeState.company.loading,
  updating: storeState.company.updating,
  updateSuccess: storeState.company.updateSuccess
});

const mapDispatchToProps = {
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
)(CompanyUpdate);
