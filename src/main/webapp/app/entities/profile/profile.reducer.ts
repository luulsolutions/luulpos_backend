import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProfile, defaultValue } from 'app/shared/model/profile.model';

export const ACTION_TYPES = {
  SEARCH_PROFILES: 'profile/SEARCH_PROFILES',
  FETCH_PROFILE_LIST: 'profile/FETCH_PROFILE_LIST',
  FETCH_PROFILE: 'profile/FETCH_PROFILE',
  CREATE_PROFILE: 'profile/CREATE_PROFILE',
  UPDATE_PROFILE: 'profile/UPDATE_PROFILE',
  DELETE_PROFILE: 'profile/DELETE_PROFILE',
  SET_BLOB: 'profile/SET_BLOB',
  RESET: 'profile/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProfile>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ProfileState = Readonly<typeof initialState>;

// Reducer

export default (state: ProfileState = initialState, action): ProfileState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PROFILES):
    case REQUEST(ACTION_TYPES.FETCH_PROFILE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PROFILE):
    case REQUEST(ACTION_TYPES.UPDATE_PROFILE):
    case REQUEST(ACTION_TYPES.DELETE_PROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PROFILES):
    case FAILURE(ACTION_TYPES.FETCH_PROFILE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROFILE):
    case FAILURE(ACTION_TYPES.CREATE_PROFILE):
    case FAILURE(ACTION_TYPES.UPDATE_PROFILE):
    case FAILURE(ACTION_TYPES.DELETE_PROFILE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PROFILES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROFILE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROFILE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROFILE):
    case SUCCESS(ACTION_TYPES.UPDATE_PROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROFILE):
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

const apiUrl = 'api/profiles';
const apiSearchUrl = 'api/_search/profiles';

// Actions

export const getSearchEntities: ICrudSearchAction<IProfile> = query => ({
  type: ACTION_TYPES.SEARCH_PROFILES,
  payload: axios.get<IProfile>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IProfile> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PROFILE_LIST,
    payload: axios.get<IProfile>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IProfile> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROFILE,
    payload: axios.get<IProfile>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROFILE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROFILE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProfile> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROFILE,
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
