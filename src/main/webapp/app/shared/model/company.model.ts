export interface ICompany {
  id?: number;
  companyName?: string;
  description?: string;
  note?: string;
  companyLogoContentType?: string;
  companyLogo?: any;
  companyLogoUrl?: string;
  active?: boolean;
}

export const defaultValue: Readonly<ICompany> = {
  active: false
};
