import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICompany, defaultValue } from 'app/shared/model/company.model';

export const ACTION_TYPES = {
  SEARCH_COMPANIES: 'company/SEARCH_COMPANIES',
  FETCH_COMPANY_LIST: 'company/FETCH_COMPANY_LIST',
  FETCH_COMPANY: 'company/FETCH_COMPANY',
  CREATE_COMPANY: 'company/CREATE_COMPANY',
  UPDATE_COMPANY: 'company/UPDATE_COMPANY',
  DELETE_COMPANY: 'company/DELETE_COMPANY',
  SET_BLOB: 'company/SET_BLOB',
  RESET: 'company/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICompany>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CompanyState = Readonly<typeof initialState>;

// Reducer

export default (state: CompanyState = initialState, action): CompanyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_COMPANIES):
    case REQUEST(ACTION_TYPES.FETCH_COMPANY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COMPANY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_COMPANY):
    case REQUEST(ACTION_TYPES.UPDATE_COMPANY):
    case REQUEST(ACTION_TYPES.DELETE_COMPANY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_COMPANIES):
    case FAILURE(ACTION_TYPES.FETCH_COMPANY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COMPANY):
    case FAILURE(ACTION_TYPES.CREATE_COMPANY):
    case FAILURE(ACTION_TYPES.UPDATE_COMPANY):
    case FAILURE(ACTION_TYPES.DELETE_COMPANY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_COMPANIES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMPANY_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMPANY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_COMPANY):
    case SUCCESS(ACTION_TYPES.UPDATE_COMPANY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_COMPANY):
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

const apiUrl = 'api/companies';
const apiSearchUrl = 'api/_search/companies';

// Actions

export const getSearchEntities: ICrudSearchAction<ICompany> = query => ({
  type: ACTION_TYPES.SEARCH_COMPANIES,
  payload: axios.get<ICompany>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ICompany> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COMPANY_LIST,
    payload: axios.get<ICompany>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICompany> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COMPANY,
    payload: axios.get<ICompany>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICompany> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COMPANY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICompany> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COMPANY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICompany> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COMPANY,
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
