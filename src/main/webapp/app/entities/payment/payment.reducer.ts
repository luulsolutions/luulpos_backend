import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPayment, defaultValue } from 'app/shared/model/payment.model';

export const ACTION_TYPES = {
  SEARCH_PAYMENTS: 'payment/SEARCH_PAYMENTS',
  FETCH_PAYMENT_LIST: 'payment/FETCH_PAYMENT_LIST',
  FETCH_PAYMENT: 'payment/FETCH_PAYMENT',
  CREATE_PAYMENT: 'payment/CREATE_PAYMENT',
  UPDATE_PAYMENT: 'payment/UPDATE_PAYMENT',
  DELETE_PAYMENT: 'payment/DELETE_PAYMENT',
  RESET: 'payment/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPayment>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PaymentState = Readonly<typeof initialState>;

// Reducer

export default (state: PaymentState = initialState, action): PaymentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PAYMENTS):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PAYMENT):
    case REQUEST(ACTION_TYPES.UPDATE_PAYMENT):
    case REQUEST(ACTION_TYPES.DELETE_PAYMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PAYMENTS):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENT):
    case FAILURE(ACTION_TYPES.CREATE_PAYMENT):
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENT):
    case FAILURE(ACTION_TYPES.DELETE_PAYMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PAYMENTS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/payments';
const apiSearchUrl = 'api/_search/payments';

// Actions

export const getSearchEntities: ICrudSearchAction<IPayment> = query => ({
  type: ACTION_TYPES.SEARCH_PAYMENTS,
  payload: axios.get<IPayment>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IPayment> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENT_LIST,
    payload: axios.get<IPayment>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPayment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENT,
    payload: axios.get<IPayment>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPayment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPayment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPayment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
