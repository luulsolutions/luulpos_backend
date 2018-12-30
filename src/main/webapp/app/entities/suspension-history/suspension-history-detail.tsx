import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './suspension-history.reducer';
import { ISuspensionHistory } from 'app/shared/model/suspension-history.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISuspensionHistoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SuspensionHistoryDetail extends React.Component<ISuspensionHistoryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { suspensionHistoryEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.suspensionHistory.detail.title">SuspensionHistory</Translate> [<b>
              {suspensionHistoryEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="suspendedDate">
                <Translate contentKey="luulposApp.suspensionHistory.suspendedDate">Suspended Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={suspensionHistoryEntity.suspendedDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="suspensionType">
                <Translate contentKey="luulposApp.suspensionHistory.suspensionType">Suspension Type</Translate>
              </span>
            </dt>
            <dd>{suspensionHistoryEntity.suspensionType}</dd>
            <dt>
              <span id="suspendedReason">
                <Translate contentKey="luulposApp.suspensionHistory.suspendedReason">Suspended Reason</Translate>
              </span>
            </dt>
            <dd>{suspensionHistoryEntity.suspendedReason}</dd>
            <dt>
              <span id="resolutionNote">
                <Translate contentKey="luulposApp.suspensionHistory.resolutionNote">Resolution Note</Translate>
              </span>
            </dt>
            <dd>{suspensionHistoryEntity.resolutionNote}</dd>
            <dt>
              <span id="unsuspensionDate">
                <Translate contentKey="luulposApp.suspensionHistory.unsuspensionDate">Unsuspension Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={suspensionHistoryEntity.unsuspensionDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="luulposApp.suspensionHistory.profile">Profile</Translate>
            </dt>
            <dd>{suspensionHistoryEntity.profileFirstName ? suspensionHistoryEntity.profileFirstName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.suspensionHistory.suspendedBy">Suspended By</Translate>
            </dt>
            <dd>{suspensionHistoryEntity.suspendedByFirstName ? suspensionHistoryEntity.suspendedByFirstName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/suspension-history" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/suspension-history/${suspensionHistoryEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ suspensionHistory }: IRootState) => ({
  suspensionHistoryEntity: suspensionHistory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SuspensionHistoryDetail);
