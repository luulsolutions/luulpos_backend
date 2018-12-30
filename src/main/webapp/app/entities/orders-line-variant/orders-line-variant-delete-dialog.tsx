import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IOrdersLineVariant } from 'app/shared/model/orders-line-variant.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './orders-line-variant.reducer';

export interface IOrdersLineVariantDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrdersLineVariantDeleteDialog extends React.Component<IOrdersLineVariantDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.ordersLineVariantEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { ordersLineVariantEntity } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody id="luulposApp.ordersLineVariant.delete.question">
          <Translate contentKey="luulposApp.ordersLineVariant.delete.question" interpolate={{ id: ordersLineVariantEntity.id }}>
            Are you sure you want to delete this OrdersLineVariant?
          </Translate>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />&nbsp;
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button id="jhi-confirm-delete-ordersLineVariant" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />&nbsp;
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ ordersLineVariant }: IRootState) => ({
  ordersLineVariantEntity: ordersLineVariant.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrdersLineVariantDeleteDialog);
