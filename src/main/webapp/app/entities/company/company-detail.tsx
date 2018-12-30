import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './company.reducer';
import { ICompany } from 'app/shared/model/company.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICompanyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CompanyDetail extends React.Component<ICompanyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { companyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.company.detail.title">Company</Translate> [<b>{companyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="companyName">
                <Translate contentKey="luulposApp.company.companyName">Company Name</Translate>
              </span>
            </dt>
            <dd>{companyEntity.companyName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.company.description">Description</Translate>
              </span>
            </dt>
            <dd>{companyEntity.description}</dd>
            <dt>
              <span id="note">
                <Translate contentKey="luulposApp.company.note">Note</Translate>
              </span>
            </dt>
            <dd>{companyEntity.note}</dd>
            <dt>
              <span id="companyLogo">
                <Translate contentKey="luulposApp.company.companyLogo">Company Logo</Translate>
              </span>
            </dt>
            <dd>
              {companyEntity.companyLogo ? (
                <div>
                  <a onClick={openFile(companyEntity.companyLogoContentType, companyEntity.companyLogo)}>
                    <img
                      src={`data:${companyEntity.companyLogoContentType};base64,${companyEntity.companyLogo}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {companyEntity.companyLogoContentType}, {byteSize(companyEntity.companyLogo)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="companyLogoUrl">
                <Translate contentKey="luulposApp.company.companyLogoUrl">Company Logo Url</Translate>
              </span>
            </dt>
            <dd>{companyEntity.companyLogoUrl}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="luulposApp.company.active">Active</Translate>
              </span>
            </dt>
            <dd>{companyEntity.active ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/entity/company" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/company/${companyEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ company }: IRootState) => ({
  companyEntity: company.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CompanyDetail);
