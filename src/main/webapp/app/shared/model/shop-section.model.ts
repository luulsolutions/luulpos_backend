export interface IShopSection {
  id?: number;
  sectionName?: string;
  description?: string;
  surchargePercentage?: number;
  surchargeFlatAmount?: number;
  usePercentage?: boolean;
  shopShopName?: string;
  shopId?: number;
}

export const defaultValue: Readonly<IShopSection> = {
  usePercentage: false
};
