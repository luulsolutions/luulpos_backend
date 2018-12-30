import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShopDevice, defaultValue } from 'app/shared/model/shop-device.model';

export const ACTION_TYPES = {
  SEARCH_SHOPDEVICES: 'shopDevice/SEARCH_SHOPDEVICES',
  FETCH_SHOPDEVICE_LIST: 'shopDevice/FETCH_SHOPDEVICE_LIST',
  FETCH_SHOPDEVICE: 'shopDevice/FETCH_SHOPDEVICE',
  CREATE_SHOPDEVICE: 'shopDevice/CREATE_SHOPDEVICE',
  UPDATE_SHOPDEVICE: 'shopDevice/UPDATE_SHOPDEVICE',
  DELETE_SHOPDEVICE: 'shopDevice/DELETE_SHOPDEVICE',
  RESET: 'shopDevice/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShopDevice>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ShopDeviceState = Readonly<typeof initialState>;

// Reducer

export default (state: ShopDeviceState = initialState, action): ShopDeviceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SHOPDEVICES):
    case REQUEST(ACTION_TYPES.FETCH_SHOPDEVICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPDEVICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPDEVICE):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPDEVICE):
    case REQUEST(ACTION_TYPES.DELETE_SHOPDEVICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SHOPDEVICES):
    case FAILURE(ACTION_TYPES.FETCH_SHOPDEVICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPDEVICE):
    case FAILURE(ACTION_TYPES.CREATE_SHOPDEVICE):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPDEVICE):
    case FAILURE(ACTION_TYPES.DELETE_SHOPDEVICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SHOPDEVICES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPDEVICE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPDEVICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPDEVICE):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPDEVICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPDEVICE):
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

const apiUrl = 'api/shop-devices';
const apiSearchUrl = 'api/_search/shop-devices';

// Actions

export const getSearchEntities: ICrudSearchAction<IShopDevice> = query => ({
  type: ACTION_TYPES.SEARCH_SHOPDEVICES,
  payload: axios.get<IShopDevice>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IShopDevice> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPDEVICE_LIST,
    payload: axios.get<IShopDevice>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IShopDevice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPDEVICE,
    payload: axios.get<IShopDevice>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShopDevice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPDEVICE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShopDevice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPDEVICE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShopDevice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPDEVICE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
