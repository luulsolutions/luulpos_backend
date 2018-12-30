import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductExtra, defaultValue } from 'app/shared/model/product-extra.model';

export const ACTION_TYPES = {
  SEARCH_PRODUCTEXTRAS: 'productExtra/SEARCH_PRODUCTEXTRAS',
  FETCH_PRODUCTEXTRA_LIST: 'productExtra/FETCH_PRODUCTEXTRA_LIST',
  FETCH_PRODUCTEXTRA: 'productExtra/FETCH_PRODUCTEXTRA',
  CREATE_PRODUCTEXTRA: 'productExtra/CREATE_PRODUCTEXTRA',
  UPDATE_PRODUCTEXTRA: 'productExtra/UPDATE_PRODUCTEXTRA',
  DELETE_PRODUCTEXTRA: 'productExtra/DELETE_PRODUCTEXTRA',
  SET_BLOB: 'productExtra/SET_BLOB',
  RESET: 'productExtra/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductExtra>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ProductExtraState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductExtraState = initialState, action): ProductExtraState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PRODUCTEXTRAS):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTEXTRA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTEXTRA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTEXTRA):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTEXTRA):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTEXTRA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PRODUCTEXTRAS):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTEXTRA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTEXTRA):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTEXTRA):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTEXTRA):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTEXTRA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PRODUCTEXTRAS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTEXTRA_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTEXTRA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTEXTRA):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTEXTRA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTEXTRA):
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

const apiUrl = 'api/product-extras';
const apiSearchUrl = 'api/_search/product-extras';

// Actions

export const getSearchEntities: ICrudSearchAction<IProductExtra> = query => ({
  type: ACTION_TYPES.SEARCH_PRODUCTEXTRAS,
  payload: axios.get<IProductExtra>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IProductExtra> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTEXTRA_LIST,
    payload: axios.get<IProductExtra>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IProductExtra> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTEXTRA,
    payload: axios.get<IProductExtra>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProductExtra> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTEXTRA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductExtra> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTEXTRA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductExtra> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTEXTRA,
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
