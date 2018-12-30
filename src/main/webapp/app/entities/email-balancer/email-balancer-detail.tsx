import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './email-balancer.reducer';
import { IEmailBalancer } from 'app/shared/model/email-balancer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmailBalancerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class EmailBalancerDetail extends React.Component<IEmailBalancerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { emailBalancerEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.emailBalancer.detail.title">EmailBalancer</Translate> [<b>{emailBalancerEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="relayId">
                <Translate contentKey="luulposApp.emailBalancer.relayId">Relay Id</Translate>
              </span>
            </dt>
            <dd>{emailBalancerEntity.relayId}</dd>
            <dt>
              <span id="relayPassword">
                <Translate contentKey="luulposApp.emailBalancer.relayPassword">Relay Password</Translate>
              </span>
            </dt>
            <dd>{emailBalancerEntity.relayPassword}</dd>
            <dt>
              <span id="startingCount">
                <Translate contentKey="luulposApp.emailBalancer.startingCount">Starting Count</Translate>
              </span>
            </dt>
            <dd>{emailBalancerEntity.startingCount}</dd>
            <dt>
              <span id="endingCount">
                <Translate contentKey="luulposApp.emailBalancer.endingCount">Ending Count</Translate>
              </span>
            </dt>
            <dd>{emailBalancerEntity.endingCount}</dd>
            <dt>
              <span id="provider">
                <Translate contentKey="luulposApp.emailBalancer.provider">Provider</Translate>
              </span>
            </dt>
            <dd>{emailBalancerEntity.provider}</dd>
            <dt>
              <span id="relayPort">
                <Translate contentKey="luulposApp.emailBalancer.relayPort">Relay Port</Translate>
              </span>
            </dt>
            <dd>{emailBalancerEntity.relayPort}</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="luulposApp.emailBalancer.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{emailBalancerEntity.enabled ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/entity/email-balancer" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/email-balancer/${emailBalancerEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ emailBalancer }: IRootState) => ({
  emailBalancerEntity: emailBalancer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EmailBalancerDetail);
