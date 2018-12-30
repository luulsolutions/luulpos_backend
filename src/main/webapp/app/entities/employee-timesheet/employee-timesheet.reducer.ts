import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEmployeeTimesheet, defaultValue } from 'app/shared/model/employee-timesheet.model';

export const ACTION_TYPES = {
  SEARCH_EMPLOYEETIMESHEETS: 'employeeTimesheet/SEARCH_EMPLOYEETIMESHEETS',
  FETCH_EMPLOYEETIMESHEET_LIST: 'employeeTimesheet/FETCH_EMPLOYEETIMESHEET_LIST',
  FETCH_EMPLOYEETIMESHEET: 'employeeTimesheet/FETCH_EMPLOYEETIMESHEET',
  CREATE_EMPLOYEETIMESHEET: 'employeeTimesheet/CREATE_EMPLOYEETIMESHEET',
  UPDATE_EMPLOYEETIMESHEET: 'employeeTimesheet/UPDATE_EMPLOYEETIMESHEET',
  DELETE_EMPLOYEETIMESHEET: 'employeeTimesheet/DELETE_EMPLOYEETIMESHEET',
  RESET: 'employeeTimesheet/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEmployeeTimesheet>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EmployeeTimesheetState = Readonly<typeof initialState>;

// Reducer

export default (state: EmployeeTimesheetState = initialState, action): EmployeeTimesheetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_EMPLOYEETIMESHEETS):
    case REQUEST(ACTION_TYPES.FETCH_EMPLOYEETIMESHEET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EMPLOYEETIMESHEET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EMPLOYEETIMESHEET):
    case REQUEST(ACTION_TYPES.UPDATE_EMPLOYEETIMESHEET):
    case REQUEST(ACTION_TYPES.DELETE_EMPLOYEETIMESHEET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_EMPLOYEETIMESHEETS):
    case FAILURE(ACTION_TYPES.FETCH_EMPLOYEETIMESHEET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EMPLOYEETIMESHEET):
    case FAILURE(ACTION_TYPES.CREATE_EMPLOYEETIMESHEET):
    case FAILURE(ACTION_TYPES.UPDATE_EMPLOYEETIMESHEET):
    case FAILURE(ACTION_TYPES.DELETE_EMPLOYEETIMESHEET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_EMPLOYEETIMESHEETS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMPLOYEETIMESHEET_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMPLOYEETIMESHEET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EMPLOYEETIMESHEET):
    case SUCCESS(ACTION_TYPES.UPDATE_EMPLOYEETIMESHEET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EMPLOYEETIMESHEET):
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

const apiUrl = 'api/employee-timesheets';
const apiSearchUrl = 'api/_search/employee-timesheets';

// Actions

export const getSearchEntities: ICrudSearchAction<IEmployeeTimesheet> = query => ({
  type: ACTION_TYPES.SEARCH_EMPLOYEETIMESHEETS,
  payload: axios.get<IEmployeeTimesheet>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IEmployeeTimesheet> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EMPLOYEETIMESHEET_LIST,
    payload: axios.get<IEmployeeTimesheet>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEmployeeTimesheet> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EMPLOYEETIMESHEET,
    payload: axios.get<IEmployeeTimesheet>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEmployeeTimesheet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EMPLOYEETIMESHEET,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEmployeeTimesheet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EMPLOYEETIMESHEET,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEmployeeTimesheet> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EMPLOYEETIMESHEET,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
