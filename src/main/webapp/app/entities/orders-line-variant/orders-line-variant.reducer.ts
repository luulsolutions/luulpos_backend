import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrdersLineVariant, defaultValue } from 'app/shared/model/orders-line-variant.model';

export const ACTION_TYPES = {
  SEARCH_ORDERSLINEVARIANTS: 'ordersLineVariant/SEARCH_ORDERSLINEVARIANTS',
  FETCH_ORDERSLINEVARIANT_LIST: 'ordersLineVariant/FETCH_ORDERSLINEVARIANT_LIST',
  FETCH_ORDERSLINEVARIANT: 'ordersLineVariant/FETCH_ORDERSLINEVARIANT',
  CREATE_ORDERSLINEVARIANT: 'ordersLineVariant/CREATE_ORDERSLINEVARIANT',
  UPDATE_ORDERSLINEVARIANT: 'ordersLineVariant/UPDATE_ORDERSLINEVARIANT',
  DELETE_ORDERSLINEVARIANT: 'ordersLineVariant/DELETE_ORDERSLINEVARIANT',
  SET_BLOB: 'ordersLineVariant/SET_BLOB',
  RESET: 'ordersLineVariant/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrdersLineVariant>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OrdersLineVariantState = Readonly<typeof initialState>;

// Reducer

export default (state: OrdersLineVariantState = initialState, action): OrdersLineVariantState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ORDERSLINEVARIANTS):
    case REQUEST(ACTION_TYPES.FETCH_ORDERSLINEVARIANT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORDERSLINEVARIANT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORDERSLINEVARIANT):
    case REQUEST(ACTION_TYPES.UPDATE_ORDERSLINEVARIANT):
    case REQUEST(ACTION_TYPES.DELETE_ORDERSLINEVARIANT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ORDERSLINEVARIANTS):
    case FAILURE(ACTION_TYPES.FETCH_ORDERSLINEVARIANT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORDERSLINEVARIANT):
    case FAILURE(ACTION_TYPES.CREATE_ORDERSLINEVARIANT):
    case FAILURE(ACTION_TYPES.UPDATE_ORDERSLINEVARIANT):
    case FAILURE(ACTION_TYPES.DELETE_ORDERSLINEVARIANT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ORDERSLINEVARIANTS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERSLINEVARIANT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERSLINEVARIANT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORDERSLINEVARIANT):
    case SUCCESS(ACTION_TYPES.UPDATE_ORDERSLINEVARIANT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORDERSLINEVARIANT):
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

const apiUrl = 'api/orders-line-variants';
const apiSearchUrl = 'api/_search/orders-line-variants';

// Actions

export const getSearchEntities: ICrudSearchAction<IOrdersLineVariant> = query => ({
  type: ACTION_TYPES.SEARCH_ORDERSLINEVARIANTS,
  payload: axios.get<IOrdersLineVariant>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IOrdersLineVariant> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERSLINEVARIANT_LIST,
    payload: axios.get<IOrdersLineVariant>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOrdersLineVariant> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERSLINEVARIANT,
    payload: axios.get<IOrdersLineVariant>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrdersLineVariant> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORDERSLINEVARIANT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrdersLineVariant> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORDERSLINEVARIANT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrdersLineVariant> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORDERSLINEVARIANT,
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
