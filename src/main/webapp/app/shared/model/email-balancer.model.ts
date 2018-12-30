export interface IEmailBalancer {
  id?: number;
  relayId?: string;
  relayPassword?: string;
  startingCount?: number;
  endingCount?: number;
  provider?: string;
  relayPort?: number;
  enabled?: boolean;
}

export const defaultValue: Readonly<IEmailBalancer> = {
  enabled: false
};
