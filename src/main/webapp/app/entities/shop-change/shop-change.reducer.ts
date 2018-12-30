import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopChange, defaultValue } from 'app/shared/model/shop-change.model';

export const ACTION_TYPES = {
  SEARCH_SHOPCHANGES: 'shopChange/SEARCH_SHOPCHANGES',
  FETCH_SHOPCHANGE_LIST: 'shopChange/FETCH_SHOPCHANGE_LIST',
  FETCH_SHOPCHANGE: 'shopChange/FETCH_SHOPCHANGE',
  CREATE_SHOPCHANGE: 'shopChange/CREATE_SHOPCHANGE',
  UPDATE_SHOPCHANGE: 'shopChange/UPDATE_SHOPCHANGE',
  DELETE_SHOPCHANGE: 'shopChange/DELETE_SHOPCHANGE',
  RESET: 'shopChange/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopChange>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ShopChangeState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopChangeState = initialState, action): ShopChangeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SHOPCHANGES):
    case REQUEST(ACTION_TYPES.FETCH_SHOPCHANGE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPCHANGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPCHANGE):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPCHANGE):
    case REQUEST(ACTION_TYPES.DELETE_SHOPCHANGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SHOPCHANGES):
    case FAILURE(ACTION_TYPES.FETCH_SHOPCHANGE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPCHANGE):
    case FAILURE(ACTION_TYPES.CREATE_SHOPCHANGE):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPCHANGE):
    case FAILURE(ACTION_TYPES.DELETE_SHOPCHANGE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SHOPCHANGES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPCHANGE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPCHANGE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPCHANGE):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPCHANGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPCHANGE):
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

const apiUrl = 'api/shop-changes';
const apiSearchUrl = 'api/_search/shop-changes';

// Actions

export const getSearchEntities: ICrudSearchAction<IShopChange> = query => ({
  type: ACTION_TYPES.SEARCH_SHOPCHANGES,
  payload: axios.get<IShopChange>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IShopChange> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPCHANGE_LIST,
    payload: axios.get<IShopChange>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IShopChange> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPCHANGE,
    payload: axios.get<IShopChange>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopChange> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPCHANGE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopChange> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPCHANGE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopChange> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPCHANGE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
