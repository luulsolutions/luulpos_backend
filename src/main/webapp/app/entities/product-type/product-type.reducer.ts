import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductType, defaultValue } from 'app/shared/model/product-type.model';

export const ACTION_TYPES = {
  SEARCH_PRODUCTTYPES: 'productType/SEARCH_PRODUCTTYPES',
  FETCH_PRODUCTTYPE_LIST: 'productType/FETCH_PRODUCTTYPE_LIST',
  FETCH_PRODUCTTYPE: 'productType/FETCH_PRODUCTTYPE',
  CREATE_PRODUCTTYPE: 'productType/CREATE_PRODUCTTYPE',
  UPDATE_PRODUCTTYPE: 'productType/UPDATE_PRODUCTTYPE',
  DELETE_PRODUCTTYPE: 'productType/DELETE_PRODUCTTYPE',
  RESET: 'productType/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductType>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ProductTypeState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductTypeState = initialState, action): ProductTypeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PRODUCTTYPES):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTTYPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTTYPE):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTTYPE):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PRODUCTTYPES):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTTYPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTTYPE):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTTYPE):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTTYPE):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTTYPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PRODUCTTYPES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTTYPE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTTYPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTTYPE):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTTYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTTYPE):
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

const apiUrl = 'api/product-types';
const apiSearchUrl = 'api/_search/product-types';

// Actions

export const getSearchEntities: ICrudSearchAction<IProductType> = query => ({
  type: ACTION_TYPES.SEARCH_PRODUCTTYPES,
  payload: axios.get<IProductType>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IProductType> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTTYPE_LIST,
    payload: axios.get<IProductType>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IProductType> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTTYPE,
    payload: axios.get<IProductType>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProductType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTTYPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTTYPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductType> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTTYPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
