import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEmailBalancer, defaultValue } from 'app/shared/model/email-balancer.model';

export const ACTION_TYPES = {
  SEARCH_EMAILBALANCERS: 'emailBalancer/SEARCH_EMAILBALANCERS',
  FETCH_EMAILBALANCER_LIST: 'emailBalancer/FETCH_EMAILBALANCER_LIST',
  FETCH_EMAILBALANCER: 'emailBalancer/FETCH_EMAILBALANCER',
  CREATE_EMAILBALANCER: 'emailBalancer/CREATE_EMAILBALANCER',
  UPDATE_EMAILBALANCER: 'emailBalancer/UPDATE_EMAILBALANCER',
  DELETE_EMAILBALANCER: 'emailBalancer/DELETE_EMAILBALANCER',
  RESET: 'emailBalancer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEmailBalancer>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EmailBalancerState = Readonly<typeof initialState>;

// Reducer

export default (state: EmailBalancerState = initialState, action): EmailBalancerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_EMAILBALANCERS):
    case REQUEST(ACTION_TYPES.FETCH_EMAILBALANCER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EMAILBALANCER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EMAILBALANCER):
    case REQUEST(ACTION_TYPES.UPDATE_EMAILBALANCER):
    case REQUEST(ACTION_TYPES.DELETE_EMAILBALANCER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_EMAILBALANCERS):
    case FAILURE(ACTION_TYPES.FETCH_EMAILBALANCER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EMAILBALANCER):
    case FAILURE(ACTION_TYPES.CREATE_EMAILBALANCER):
    case FAILURE(ACTION_TYPES.UPDATE_EMAILBALANCER):
    case FAILURE(ACTION_TYPES.DELETE_EMAILBALANCER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_EMAILBALANCERS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAILBALANCER_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMAILBALANCER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EMAILBALANCER):
    case SUCCESS(ACTION_TYPES.UPDATE_EMAILBALANCER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EMAILBALANCER):
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

const apiUrl = 'api/email-balancers';
const apiSearchUrl = 'api/_search/email-balancers';

// Actions

export const getSearchEntities: ICrudSearchAction<IEmailBalancer> = query => ({
  type: ACTION_TYPES.SEARCH_EMAILBALANCERS,
  payload: axios.get<IEmailBalancer>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IEmailBalancer> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EMAILBALANCER_LIST,
    payload: axios.get<IEmailBalancer>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEmailBalancer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EMAILBALANCER,
    payload: axios.get<IEmailBalancer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEmailBalancer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EMAILBALANCER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEmailBalancer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EMAILBALANCER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEmailBalancer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EMAILBALANCER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
