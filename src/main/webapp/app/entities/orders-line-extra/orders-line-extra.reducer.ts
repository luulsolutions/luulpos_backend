import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrdersLineExtra, defaultValue } from 'app/shared/model/orders-line-extra.model';

export const ACTION_TYPES = {
  SEARCH_ORDERSLINEEXTRAS: 'ordersLineExtra/SEARCH_ORDERSLINEEXTRAS',
  FETCH_ORDERSLINEEXTRA_LIST: 'ordersLineExtra/FETCH_ORDERSLINEEXTRA_LIST',
  FETCH_ORDERSLINEEXTRA: 'ordersLineExtra/FETCH_ORDERSLINEEXTRA',
  CREATE_ORDERSLINEEXTRA: 'ordersLineExtra/CREATE_ORDERSLINEEXTRA',
  UPDATE_ORDERSLINEEXTRA: 'ordersLineExtra/UPDATE_ORDERSLINEEXTRA',
  DELETE_ORDERSLINEEXTRA: 'ordersLineExtra/DELETE_ORDERSLINEEXTRA',
  SET_BLOB: 'ordersLineExtra/SET_BLOB',
  RESET: 'ordersLineExtra/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrdersLineExtra>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OrdersLineExtraState = Readonly<typeof initialState>;

// Reducer

export default (state: OrdersLineExtraState = initialState, action): OrdersLineExtraState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ORDERSLINEEXTRAS):
    case REQUEST(ACTION_TYPES.FETCH_ORDERSLINEEXTRA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORDERSLINEEXTRA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORDERSLINEEXTRA):
    case REQUEST(ACTION_TYPES.UPDATE_ORDERSLINEEXTRA):
    case REQUEST(ACTION_TYPES.DELETE_ORDERSLINEEXTRA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ORDERSLINEEXTRAS):
    case FAILURE(ACTION_TYPES.FETCH_ORDERSLINEEXTRA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORDERSLINEEXTRA):
    case FAILURE(ACTION_TYPES.CREATE_ORDERSLINEEXTRA):
    case FAILURE(ACTION_TYPES.UPDATE_ORDERSLINEEXTRA):
    case FAILURE(ACTION_TYPES.DELETE_ORDERSLINEEXTRA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ORDERSLINEEXTRAS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERSLINEEXTRA_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERSLINEEXTRA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORDERSLINEEXTRA):
    case SUCCESS(ACTION_TYPES.UPDATE_ORDERSLINEEXTRA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORDERSLINEEXTRA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/orders-line-extras';
const apiSearchUrl = 'api/_search/orders-line-extras';

// Actions

export const getSearchEntities: ICrudSearchAction<IOrdersLineExtra> = query => ({
  type: ACTION_TYPES.SEARCH_ORDERSLINEEXTRAS,
  payload: axios.get<IOrdersLineExtra>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IOrdersLineExtra> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERSLINEEXTRA_LIST,
    payload: axios.get<IOrdersLineExtra>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOrdersLineExtra> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERSLINEEXTRA,
    payload: axios.get<IOrdersLineExtra>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrdersLineExtra> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORDERSLINEEXTRA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrdersLineExtra> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORDERSLINEEXTRA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrdersLineExtra> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORDERSLINEEXTRA,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
