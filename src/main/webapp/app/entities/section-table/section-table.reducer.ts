import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISectionTable, defaultValue } from 'app/shared/model/section-table.model';

export const ACTION_TYPES = {
  SEARCH_SECTIONTABLES: 'sectionTable/SEARCH_SECTIONTABLES',
  FETCH_SECTIONTABLE_LIST: 'sectionTable/FETCH_SECTIONTABLE_LIST',
  FETCH_SECTIONTABLE: 'sectionTable/FETCH_SECTIONTABLE',
  CREATE_SECTIONTABLE: 'sectionTable/CREATE_SECTIONTABLE',
  UPDATE_SECTIONTABLE: 'sectionTable/UPDATE_SECTIONTABLE',
  DELETE_SECTIONTABLE: 'sectionTable/DELETE_SECTIONTABLE',
  RESET: 'sectionTable/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISectionTable>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SectionTableState = Readonly<typeof initialState>;

// Reducer

export default (state: SectionTableState = initialState, action): SectionTableState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SECTIONTABLES):
    case REQUEST(ACTION_TYPES.FETCH_SECTIONTABLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SECTIONTABLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SECTIONTABLE):
    case REQUEST(ACTION_TYPES.UPDATE_SECTIONTABLE):
    case REQUEST(ACTION_TYPES.DELETE_SECTIONTABLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SECTIONTABLES):
    case FAILURE(ACTION_TYPES.FETCH_SECTIONTABLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SECTIONTABLE):
    case FAILURE(ACTION_TYPES.CREATE_SECTIONTABLE):
    case FAILURE(ACTION_TYPES.UPDATE_SECTIONTABLE):
    case FAILURE(ACTION_TYPES.DELETE_SECTIONTABLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SECTIONTABLES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SECTIONTABLE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SECTIONTABLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SECTIONTABLE):
    case SUCCESS(ACTION_TYPES.UPDATE_SECTIONTABLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SECTIONTABLE):
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

const apiUrl = 'api/section-tables';
const apiSearchUrl = 'api/_search/section-tables';

// Actions

export const getSearchEntities: ICrudSearchAction<ISectionTable> = query => ({
  type: ACTION_TYPES.SEARCH_SECTIONTABLES,
  payload: axios.get<ISectionTable>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ISectionTable> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SECTIONTABLE_LIST,
    payload: axios.get<ISectionTable>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISectionTable> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SECTIONTABLE,
    payload: axios.get<ISectionTable>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISectionTable> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SECTIONTABLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISectionTable> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SECTIONTABLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISectionTable> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SECTIONTABLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
