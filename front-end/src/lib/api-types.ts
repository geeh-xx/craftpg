export type Campaign = {
  id: string;
  title: string;
  description?: string;
  system: string;
  frequency: string;
  status: string;
  progressPercent: number;
};

export type CharacterBase = {
  id: string;
  name: string;
  race?: string;
  clazz?: string;
  attributesJson?: string;
};

export type InvitePreview = {
  campaignId: string;
  campaignTitle: string;
  dmName: string;
  email: string;
  roles: string[];
  expiresAt: string;
  accepted: boolean;
};

export type PendingInvite = {
  id: string;
  campaignId: string;
  email: string;
  roles: string[];
  createdAt: string;
  expiresAt: string;
};

export type SessionRecord = {
  id: string;
  campaignId: string;
  title: string;
  scheduledAt: string;
  summary?: string;
  notes?: string;
};

export type CampaignCharacter = {
  id: string;
  campaignId: string;
  characterBaseId: string;
  userId: string;
  level: number;
  xp: number;
  sheetStateJson?: string;
  inventoryJson?: string;
  locked: boolean;
};

export type NotificationItem = {
  id: string;
  userId: string;
  type: string;
  payloadJson: string;
  createdAt: string;
  readAt?: string;
  read: boolean;
};
