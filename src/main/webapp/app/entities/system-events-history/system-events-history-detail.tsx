import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './system-events-history.reducer';
import { ISystemEventsHistory } from 'app/shared/model/system-events-history.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISystemEventsHistoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SystemEventsHistoryDetail extends React.Component<ISystemEventsHistoryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { systemEventsHistoryEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.systemEventsHistory.detail.title">SystemEventsHistory</Translate> [<b>
              {systemEventsHistoryEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="eventName">
                <Translate contentKey="luulposApp.systemEventsHistory.eventName">Event Name</Translate>
              </span>
            </dt>
            <dd>{systemEventsHistoryEntity.eventName}</dd>
            <dt>
              <span id="eventDate">
                <Translate contentKey="luulposApp.systemEventsHistory.eventDate">Event Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={systemEventsHistoryEntity.eventDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="eventApi">
                <Translate contentKey="luulposApp.systemEventsHistory.eventApi">Event Api</Translate>
              </span>
            </dt>
            <dd>{systemEventsHistoryEntity.eventApi}</dd>
            <dt>
              <span id="eventNote">
                <Translate contentKey="luulposApp.systemEventsHistory.eventNote">Event Note</Translate>
              </span>
            </dt>
            <dd>{systemEventsHistoryEntity.eventNote}</dd>
            <dt>
              <span id="eventEntityName">
                <Translate contentKey="luulposApp.systemEventsHistory.eventEntityName">Event Entity Name</Translate>
              </span>
            </dt>
            <dd>{systemEventsHistoryEntity.eventEntityName}</dd>
            <dt>
              <span id="eventEntityId">
                <Translate contentKey="luulposApp.systemEventsHistory.eventEntityId">Event Entity Id</Translate>
              </span>
            </dt>
            <dd>{systemEventsHistoryEntity.eventEntityId}</dd>
            <dt>
              <Translate contentKey="luulposApp.systemEventsHistory.triggedBy">Trigged By</Translate>
            </dt>
            <dd>{systemEventsHistoryEntity.triggedByFirstName ? systemEventsHistoryEntity.triggedByFirstName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/system-events-history" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/system-events-history/${systemEventsHistoryEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ systemEventsHistory }: IRootState) => ({
  systemEventsHistoryEntity: systemEventsHistory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SystemEventsHistoryDetail);
