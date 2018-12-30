import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPaymentMethod, defaultValue } from 'app/shared/model/payment-method.model';

export const ACTION_TYPES = {
  SEARCH_PAYMENTMETHODS: 'paymentMethod/SEARCH_PAYMENTMETHODS',
  FETCH_PAYMENTMETHOD_LIST: 'paymentMethod/FETCH_PAYMENTMETHOD_LIST',
  FETCH_PAYMENTMETHOD: 'paymentMethod/FETCH_PAYMENTMETHOD',
  CREATE_PAYMENTMETHOD: 'paymentMethod/CREATE_PAYMENTMETHOD',
  UPDATE_PAYMENTMETHOD: 'paymentMethod/UPDATE_PAYMENTMETHOD',
  DELETE_PAYMENTMETHOD: 'paymentMethod/DELETE_PAYMENTMETHOD',
  RESET: 'paymentMethod/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPaymentMethod>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PaymentMethodState = Readonly<typeof initialState>;

// Reducer

export default (state: PaymentMethodState = initialState, action): PaymentMethodState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PAYMENTMETHODS):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTMETHOD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTMETHOD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PAYMENTMETHOD):
    case REQUEST(ACTION_TYPES.UPDATE_PAYMENTMETHOD):
    case REQUEST(ACTION_TYPES.DELETE_PAYMENTMETHOD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PAYMENTMETHODS):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTMETHOD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTMETHOD):
    case FAILURE(ACTION_TYPES.CREATE_PAYMENTMETHOD):
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENTMETHOD):
    case FAILURE(ACTION_TYPES.DELETE_PAYMENTMETHOD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PAYMENTMETHODS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTMETHOD_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTMETHOD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYMENTMETHOD):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENTMETHOD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYMENTMETHOD):
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

const apiUrl = 'api/payment-methods';
const apiSearchUrl = 'api/_search/payment-methods';

// Actions

export const getSearchEntities: ICrudSearchAction<IPaymentMethod> = query => ({
  type: ACTION_TYPES.SEARCH_PAYMENTMETHODS,
  payload: axios.get<IPaymentMethod>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IPaymentMethod> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTMETHOD_LIST,
    payload: axios.get<IPaymentMethod>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPaymentMethod> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTMETHOD,
    payload: axios.get<IPaymentMethod>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPaymentMethod> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYMENTMETHOD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPaymentMethod> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTMETHOD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPaymentMethod> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYMENTMETHOD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
