import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISuspensionHistory, defaultValue } from 'app/shared/model/suspension-history.model';

export const ACTION_TYPES = {
  SEARCH_SUSPENSIONHISTORIES: 'suspensionHistory/SEARCH_SUSPENSIONHISTORIES',
  FETCH_SUSPENSIONHISTORY_LIST: 'suspensionHistory/FETCH_SUSPENSIONHISTORY_LIST',
  FETCH_SUSPENSIONHISTORY: 'suspensionHistory/FETCH_SUSPENSIONHISTORY',
  CREATE_SUSPENSIONHISTORY: 'suspensionHistory/CREATE_SUSPENSIONHISTORY',
  UPDATE_SUSPENSIONHISTORY: 'suspensionHistory/UPDATE_SUSPENSIONHISTORY',
  DELETE_SUSPENSIONHISTORY: 'suspensionHistory/DELETE_SUSPENSIONHISTORY',
  RESET: 'suspensionHistory/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISuspensionHistory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SuspensionHistoryState = Readonly<typeof initialState>;

// Reducer

export default (state: SuspensionHistoryState = initialState, action): SuspensionHistoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SUSPENSIONHISTORIES):
    case REQUEST(ACTION_TYPES.FETCH_SUSPENSIONHISTORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUSPENSIONHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUSPENSIONHISTORY):
    case REQUEST(ACTION_TYPES.UPDATE_SUSPENSIONHISTORY):
    case REQUEST(ACTION_TYPES.DELETE_SUSPENSIONHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SUSPENSIONHISTORIES):
    case FAILURE(ACTION_TYPES.FETCH_SUSPENSIONHISTORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUSPENSIONHISTORY):
    case FAILURE(ACTION_TYPES.CREATE_SUSPENSIONHISTORY):
    case FAILURE(ACTION_TYPES.UPDATE_SUSPENSIONHISTORY):
    case FAILURE(ACTION_TYPES.DELETE_SUSPENSIONHISTORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SUSPENSIONHISTORIES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUSPENSIONHISTORY_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUSPENSIONHISTORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUSPENSIONHISTORY):
    case SUCCESS(ACTION_TYPES.UPDATE_SUSPENSIONHISTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUSPENSIONHISTORY):
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

const apiUrl = 'api/suspension-histories';
const apiSearchUrl = 'api/_search/suspension-histories';

// Actions

export const getSearchEntities: ICrudSearchAction<ISuspensionHistory> = query => ({
  type: ACTION_TYPES.SEARCH_SUSPENSIONHISTORIES,
  payload: axios.get<ISuspensionHistory>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ISuspensionHistory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SUSPENSIONHISTORY_LIST,
    payload: axios.get<ISuspensionHistory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISuspensionHistory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUSPENSIONHISTORY,
    payload: axios.get<ISuspensionHistory>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISuspensionHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUSPENSIONHISTORY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISuspensionHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUSPENSIONHISTORY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISuspensionHistory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUSPENSIONHISTORY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
