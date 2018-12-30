import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDiscount, defaultValue } from 'app/shared/model/discount.model';

export const ACTION_TYPES = {
  SEARCH_DISCOUNTS: 'discount/SEARCH_DISCOUNTS',
  FETCH_DISCOUNT_LIST: 'discount/FETCH_DISCOUNT_LIST',
  FETCH_DISCOUNT: 'discount/FETCH_DISCOUNT',
  CREATE_DISCOUNT: 'discount/CREATE_DISCOUNT',
  UPDATE_DISCOUNT: 'discount/UPDATE_DISCOUNT',
  DELETE_DISCOUNT: 'discount/DELETE_DISCOUNT',
  RESET: 'discount/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDiscount>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DiscountState = Readonly<typeof initialState>;

// Reducer

export default (state: DiscountState = initialState, action): DiscountState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_DISCOUNTS):
    case REQUEST(ACTION_TYPES.FETCH_DISCOUNT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DISCOUNT):
    case REQUEST(ACTION_TYPES.UPDATE_DISCOUNT):
    case REQUEST(ACTION_TYPES.DELETE_DISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_DISCOUNTS):
    case FAILURE(ACTION_TYPES.FETCH_DISCOUNT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DISCOUNT):
    case FAILURE(ACTION_TYPES.CREATE_DISCOUNT):
    case FAILURE(ACTION_TYPES.UPDATE_DISCOUNT):
    case FAILURE(ACTION_TYPES.DELETE_DISCOUNT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_DISCOUNTS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DISCOUNT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DISCOUNT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DISCOUNT):
    case SUCCESS(ACTION_TYPES.UPDATE_DISCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DISCOUNT):
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

const apiUrl = 'api/discounts';
const apiSearchUrl = 'api/_search/discounts';

// Actions

export const getSearchEntities: ICrudSearchAction<IDiscount> = query => ({
  type: ACTION_TYPES.SEARCH_DISCOUNTS,
  payload: axios.get<IDiscount>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IDiscount> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DISCOUNT_LIST,
    payload: axios.get<IDiscount>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDiscount> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DISCOUNT,
    payload: axios.get<IDiscount>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DISCOUNT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DISCOUNT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDiscount> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DISCOUNT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
