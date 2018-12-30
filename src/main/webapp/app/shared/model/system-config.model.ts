export const enum ConfigType {
  STRING = 'STRING',
  BOOLEAN = 'BOOLEAN',
  NUMBER = 'NUMBER',
  DATE = 'DATE',
  FILE = 'FILE',
  OBJECT = 'OBJECT',
  ARRAY = 'ARRAY',
  GEO_POINT = 'GEO_POINT'
}

export interface ISystemConfig {
  id?: number;
  key?: string;
  value?: string;
  configurationType?: ConfigType;
  note?: string;
  enabled?: boolean;
  shopShopName?: string;
  shopId?: number;
}

export const defaultValue: Readonly<ISystemConfig> = {
  enabled: false
};
