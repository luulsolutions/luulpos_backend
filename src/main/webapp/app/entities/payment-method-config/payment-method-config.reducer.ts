import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPaymentMethodConfig, defaultValue } from 'app/shared/model/payment-method-config.model';

export const ACTION_TYPES = {
  SEARCH_PAYMENTMETHODCONFIGS: 'paymentMethodConfig/SEARCH_PAYMENTMETHODCONFIGS',
  FETCH_PAYMENTMETHODCONFIG_LIST: 'paymentMethodConfig/FETCH_PAYMENTMETHODCONFIG_LIST',
  FETCH_PAYMENTMETHODCONFIG: 'paymentMethodConfig/FETCH_PAYMENTMETHODCONFIG',
  CREATE_PAYMENTMETHODCONFIG: 'paymentMethodConfig/CREATE_PAYMENTMETHODCONFIG',
  UPDATE_PAYMENTMETHODCONFIG: 'paymentMethodConfig/UPDATE_PAYMENTMETHODCONFIG',
  DELETE_PAYMENTMETHODCONFIG: 'paymentMethodConfig/DELETE_PAYMENTMETHODCONFIG',
  RESET: 'paymentMethodConfig/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPaymentMethodConfig>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PaymentMethodConfigState = Readonly<typeof initialState>;

// Reducer

export default (state: PaymentMethodConfigState = initialState, action): PaymentMethodConfigState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PAYMENTMETHODCONFIGS):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PAYMENTMETHODCONFIG):
    case REQUEST(ACTION_TYPES.UPDATE_PAYMENTMETHODCONFIG):
    case REQUEST(ACTION_TYPES.DELETE_PAYMENTMETHODCONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PAYMENTMETHODCONFIGS):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG):
    case FAILURE(ACTION_TYPES.CREATE_PAYMENTMETHODCONFIG):
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENTMETHODCONFIG):
    case FAILURE(ACTION_TYPES.DELETE_PAYMENTMETHODCONFIG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PAYMENTMETHODCONFIGS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYMENTMETHODCONFIG):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENTMETHODCONFIG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYMENTMETHODCONFIG):
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

const apiUrl = 'api/payment-method-configs';
const apiSearchUrl = 'api/_search/payment-method-configs';

// Actions

export const getSearchEntities: ICrudSearchAction<IPaymentMethodConfig> = query => ({
  type: ACTION_TYPES.SEARCH_PAYMENTMETHODCONFIGS,
  payload: axios.get<IPaymentMethodConfig>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IPaymentMethodConfig> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG_LIST,
    payload: axios.get<IPaymentMethodConfig>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPaymentMethodConfig> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTMETHODCONFIG,
    payload: axios.get<IPaymentMethodConfig>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPaymentMethodConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYMENTMETHODCONFIG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPaymentMethodConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTMETHODCONFIG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPaymentMethodConfig> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYMENTMETHODCONFIG,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
