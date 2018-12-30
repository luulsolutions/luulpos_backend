import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrdersLine, defaultValue } from 'app/shared/model/orders-line.model';

export const ACTION_TYPES = {
  SEARCH_ORDERSLINES: 'ordersLine/SEARCH_ORDERSLINES',
  FETCH_ORDERSLINE_LIST: 'ordersLine/FETCH_ORDERSLINE_LIST',
  FETCH_ORDERSLINE: 'ordersLine/FETCH_ORDERSLINE',
  CREATE_ORDERSLINE: 'ordersLine/CREATE_ORDERSLINE',
  UPDATE_ORDERSLINE: 'ordersLine/UPDATE_ORDERSLINE',
  DELETE_ORDERSLINE: 'ordersLine/DELETE_ORDERSLINE',
  SET_BLOB: 'ordersLine/SET_BLOB',
  RESET: 'ordersLine/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrdersLine>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OrdersLineState = Readonly<typeof initialState>;

// Reducer

export default (state: OrdersLineState = initialState, action): OrdersLineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ORDERSLINES):
    case REQUEST(ACTION_TYPES.FETCH_ORDERSLINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORDERSLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORDERSLINE):
    case REQUEST(ACTION_TYPES.UPDATE_ORDERSLINE):
    case REQUEST(ACTION_TYPES.DELETE_ORDERSLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ORDERSLINES):
    case FAILURE(ACTION_TYPES.FETCH_ORDERSLINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORDERSLINE):
    case FAILURE(ACTION_TYPES.CREATE_ORDERSLINE):
    case FAILURE(ACTION_TYPES.UPDATE_ORDERSLINE):
    case FAILURE(ACTION_TYPES.DELETE_ORDERSLINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ORDERSLINES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERSLINE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERSLINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORDERSLINE):
    case SUCCESS(ACTION_TYPES.UPDATE_ORDERSLINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORDERSLINE):
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

const apiUrl = 'api/orders-lines';
const apiSearchUrl = 'api/_search/orders-lines';

// Actions

export const getSearchEntities: ICrudSearchAction<IOrdersLine> = query => ({
  type: ACTION_TYPES.SEARCH_ORDERSLINES,
  payload: axios.get<IOrdersLine>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IOrdersLine> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERSLINE_LIST,
    payload: axios.get<IOrdersLine>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOrdersLine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERSLINE,
    payload: axios.get<IOrdersLine>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrdersLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORDERSLINE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrdersLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORDERSLINE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrdersLine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORDERSLINE,
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
