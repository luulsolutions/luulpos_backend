import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopSection, defaultValue } from 'app/shared/model/shop-section.model';

export const ACTION_TYPES = {
  SEARCH_SHOPSECTIONS: 'shopSection/SEARCH_SHOPSECTIONS',
  FETCH_SHOPSECTION_LIST: 'shopSection/FETCH_SHOPSECTION_LIST',
  FETCH_SHOPSECTION: 'shopSection/FETCH_SHOPSECTION',
  CREATE_SHOPSECTION: 'shopSection/CREATE_SHOPSECTION',
  UPDATE_SHOPSECTION: 'shopSection/UPDATE_SHOPSECTION',
  DELETE_SHOPSECTION: 'shopSection/DELETE_SHOPSECTION',
  RESET: 'shopSection/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopSection>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ShopSectionState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopSectionState = initialState, action): ShopSectionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SHOPSECTIONS):
    case REQUEST(ACTION_TYPES.FETCH_SHOPSECTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPSECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPSECTION):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPSECTION):
    case REQUEST(ACTION_TYPES.DELETE_SHOPSECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SHOPSECTIONS):
    case FAILURE(ACTION_TYPES.FETCH_SHOPSECTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPSECTION):
    case FAILURE(ACTION_TYPES.CREATE_SHOPSECTION):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPSECTION):
    case FAILURE(ACTION_TYPES.DELETE_SHOPSECTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SHOPSECTIONS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPSECTION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPSECTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPSECTION):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPSECTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPSECTION):
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

const apiUrl = 'api/shop-sections';
const apiSearchUrl = 'api/_search/shop-sections';

// Actions

export const getSearchEntities: ICrudSearchAction<IShopSection> = query => ({
  type: ACTION_TYPES.SEARCH_SHOPSECTIONS,
  payload: axios.get<IShopSection>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IShopSection> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPSECTION_LIST,
    payload: axios.get<IShopSection>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IShopSection> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPSECTION,
    payload: axios.get<IShopSection>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopSection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPSECTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopSection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPSECTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopSection> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPSECTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
