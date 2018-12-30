import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISystemConfig, defaultValue } from 'app/shared/model/system-config.model';

export const ACTION_TYPES = {
  SEARCH_SYSTEMCONFIGS: 'systemConfig/SEARCH_SYSTEMCONFIGS',
  FETCH_SYSTEMCONFIG_LIST: 'systemConfig/FETCH_SYSTEMCONFIG_LIST',
  FETCH_SYSTEMCONFIG: 'systemConfig/FETCH_SYSTEMCONFIG',
  CREATE_SYSTEMCONFIG: 'systemConfig/CREATE_SYSTEMCONFIG',
  UPDATE_SYSTEMCONFIG: 'systemConfig/UPDATE_SYSTEMCONFIG',
  DELETE_SYSTEMCONFIG: 'systemConfig/DELETE_SYSTEMCONFIG',
  RESET: 'systemConfig/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISystemConfig>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SystemConfigState = Readonly<typeof initialState>;

// Reducer

export default (state: SystemConfigState = initialState, action): SystemConfigState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SYSTEMCONFIGS):
    case REQUEST(ACTION_TYPES.FETCH_SYSTEMCONFIG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SYSTEMCONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SYSTEMCONFIG):
    case REQUEST(ACTION_TYPES.UPDATE_SYSTEMCONFIG):
    case REQUEST(ACTION_TYPES.DELETE_SYSTEMCONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SYSTEMCONFIGS):
    case FAILURE(ACTION_TYPES.FETCH_SYSTEMCONFIG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SYSTEMCONFIG):
    case FAILURE(ACTION_TYPES.CREATE_SYSTEMCONFIG):
    case FAILURE(ACTION_TYPES.UPDATE_SYSTEMCONFIG):
    case FAILURE(ACTION_TYPES.DELETE_SYSTEMCONFIG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SYSTEMCONFIGS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SYSTEMCONFIG_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SYSTEMCONFIG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SYSTEMCONFIG):
    case SUCCESS(ACTION_TYPES.UPDATE_SYSTEMCONFIG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SYSTEMCONFIG):
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

const apiUrl = 'api/system-configs';
const apiSearchUrl = 'api/_search/system-configs';

// Actions

export const getSearchEntities: ICrudSearchAction<ISystemConfig> = query => ({
  type: ACTION_TYPES.SEARCH_SYSTEMCONFIGS,
  payload: axios.get<ISystemConfig>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ISystemConfig> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SYSTEMCONFIG_LIST,
    payload: axios.get<ISystemConfig>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISystemConfig> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SYSTEMCONFIG,
    payload: axios.get<ISystemConfig>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISystemConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SYSTEMCONFIG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISystemConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SYSTEMCONFIG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISystemConfig> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SYSTEMCONFIG,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
