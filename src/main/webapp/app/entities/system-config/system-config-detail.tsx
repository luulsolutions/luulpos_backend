import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './system-config.reducer';
import { ISystemConfig } from 'app/shared/model/system-config.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISystemConfigDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SystemConfigDetail extends React.Component<ISystemConfigDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { systemConfigEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.systemConfig.detail.title">SystemConfig</Translate> [<b>{systemConfigEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="key">
                <Translate contentKey="luulposApp.systemConfig.key">Key</Translate>
              </span>
            </dt>
            <dd>{systemConfigEntity.key}</dd>
            <dt>
              <span id="value">
                <Translate contentKey="luulposApp.systemConfig.value">Value</Translate>
              </span>
            </dt>
            <dd>{systemConfigEntity.value}</dd>
            <dt>
              <span id="configurationType">
                <Translate contentKey="luulposApp.systemConfig.configurationType">Configuration Type</Translate>
              </span>
            </dt>
            <dd>{systemConfigEntity.configurationType}</dd>
            <dt>
              <span id="note">
                <Translate contentKey="luulposApp.systemConfig.note">Note</Translate>
              </span>
            </dt>
            <dd>{systemConfigEntity.note}</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="luulposApp.systemConfig.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{systemConfigEntity.enabled ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.systemConfig.shop">Shop</Translate>
            </dt>
            <dd>{systemConfigEntity.shopShopName ? systemConfigEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/system-config" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/system-config/${systemConfigEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ systemConfig }: IRootState) => ({
  systemConfigEntity: systemConfig.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SystemConfigDetail);
