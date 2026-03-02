import { revalidatePath } from "next/cache";
import { Badge, Button, Card, Label, Progress, Select, Table, TableBody, TableCell, TableHead, TableHeadCell, TableRow, Tabs, TabItem, TextInput, Textarea } from "flowbite-react";
import { apiFetch } from "@/lib/api-client";
import { Campaign, CampaignCharacter, PendingInvite, SessionRecord } from "@/lib/api-types";
import { ConfirmSubmitButton } from "@/components/confirm-submit-button";

type CampaignPermissions = {
  canManageSessions: boolean;
  canInvite: boolean;
};

type CampaignPermissionsApi = {
  canManageSessions?: boolean;
  canInvite?: boolean;
  isGM?: boolean;
  can_manage_sessions?: boolean;
  can_invite?: boolean;
};

type Params = Promise<{ campaignId: string }>;

async function updateCampaign(campaignId: string, formData: FormData) {
  "use server";
  await apiFetch<Campaign>(`/campaigns/${campaignId}`, {
    method: "PATCH",
    body: JSON.stringify({
      title: String(formData.get("title") || ""),
      description: String(formData.get("description") || ""),
      frequency: String(formData.get("frequency") || "weekly"),
      status: String(formData.get("status") || "not_started"),
      progressPercent: Number(formData.get("progressPercent") || 0),
    }),
  });
  revalidatePath(`/app/campaigns/${campaignId}`);
  revalidatePath("/app/campaigns");
}

async function createInvite(campaignId: string, formData: FormData) {
  "use server";
  const selectedRoles = formData.getAll("roles").map((v) => String(v));
  await apiFetch<{ token: string }>(`/campaigns/${campaignId}/invites`, {
    method: "POST",
    body: JSON.stringify({
      email: String(formData.get("email") || ""),
      roles: selectedRoles.length ? selectedRoles : ["PLAYER"],
    }),
  });
  revalidatePath(`/app/campaigns/${campaignId}`);
}

async function cancelInvite(campaignId: string, formData: FormData) {
  "use server";
  const inviteId = String(formData.get("inviteId") || "");
  if (!inviteId) {
    return;
  }
  await apiFetch<void>(`/campaigns/${campaignId}/invites/${inviteId}`, {
    method: "DELETE",
  });
  revalidatePath(`/app/campaigns/${campaignId}`);
}

async function updateCampaignCharacter(campaignId: string, formData: FormData) {
  "use server";
  await apiFetch<CampaignCharacter>(`/campaigns/${campaignId}/characters/me`, {
    method: "PATCH",
    body: JSON.stringify({
      sheetStateJson: String(formData.get("sheetStateJson") || "{}"),
      inventoryJson: String(formData.get("inventoryJson") || "[]"),
    }),
  });
  revalidatePath(`/app/campaigns/${campaignId}`);
}

async function addXp(campaignId: string, formData: FormData) {
  "use server";
  const campaignCharacterId = String(formData.get("campaignCharacterId") || "");
  await apiFetch<CampaignCharacter>(`/campaigns/${campaignId}/characters/${campaignCharacterId}/xp`, {
    method: "POST",
    body: JSON.stringify({ gainedXp: Number(formData.get("gainedXp") || 0) }),
  });
  revalidatePath(`/app/campaigns/${campaignId}`);
}

function statusLabel(status: string) {
  if (status === "in_progress") return "in progress";
  if (status === "not_started") return "not started";
  return status.replaceAll("_", " ");
}

function normalizePermissions(raw: CampaignPermissionsApi): CampaignPermissions {
  const canManageSessions = raw.canManageSessions ?? raw.can_manage_sessions ?? false;
  const canInvite = raw.canInvite ?? raw.can_invite ?? false;
  return {
    canManageSessions,
    canInvite,
  };
}

export default async function CampaignDetailPage({ params }: { params: Params }) {
  const { campaignId } = await params;

  const campaign = await apiFetch<Campaign>(`/campaigns/${campaignId}`);

  let sessions: SessionRecord[] = [];
  try {
    sessions = await apiFetch<SessionRecord[]>(`/campaigns/${campaignId}/sessions`);
  } catch {
    sessions = [];
  }

  let permissions: CampaignPermissions = { canManageSessions: false, canInvite: false };
  let permissionsRaw: CampaignPermissionsApi | null = null;
  try {
    permissionsRaw = await apiFetch<CampaignPermissionsApi>(`/campaigns/${campaignId}/permissions`);
    permissions = normalizePermissions(permissionsRaw);
  } catch {
    permissions = { canManageSessions: false, canInvite: false };
    permissionsRaw = null;
  }

  const canCreateSession = permissions.canManageSessions || permissions.canInvite;

  let pendingInvites: PendingInvite[] = [];
  try {
    pendingInvites = await apiFetch<PendingInvite[]>(`/campaigns/${campaignId}/invites`);
  } catch {
    pendingInvites = [];
  }

  let campaignCharacter: CampaignCharacter | null = null;
  try {
    campaignCharacter = await apiFetch<CampaignCharacter>(`/campaigns/${campaignId}/characters/me`);
  } catch {
    campaignCharacter = null;
  }

  const isGameMaster = Boolean(
    permissionsRaw?.isGM,
  );

  return (
    <section className="space-y-6">
      <div className="space-y-3">
        <div className="flex flex-wrap items-center justify-between gap-2">
          <h1 className="text-3xl font-semibold">{campaign.title}</h1>
          <div className="flex items-center gap-2">
            <Badge color="info">{campaign.frequency}</Badge>
            <Badge color={campaign.status === "finished" ? "success" : "info"}>{statusLabel(campaign.status)}</Badge>
          </div>
        </div>
        <Progress progress={campaign.progressPercent} color="blue" />
      </div>

      <Tabs aria-label="campaign-detail-tabs" variant="underline">
        <TabItem active title="Overview">
          <Card>
            <h2 className="text-xl font-semibold">Overview</h2>
            <p className="text-sm text-slate-600">{campaign.description || "No description"}</p>
            <p className="text-sm text-slate-600">System: {campaign.system}</p>
            <form id="campaign-overview-form" action={updateCampaign.bind(null, campaignId)} className="mt-4 grid grid-cols-1 gap-4 md:grid-cols-2">
              <div className="md:col-span-2">
                <Label htmlFor="title">Title</Label>
                <TextInput id="title" name="title" defaultValue={campaign.title} required />
              </div>
              <div className="md:col-span-2">
                <Label htmlFor="description">Description</Label>
                <Textarea id="description" name="description" defaultValue={campaign.description || ""} rows={3} />
              </div>
              <div>
                <Label htmlFor="frequency">Frequency</Label>
                <Select id="frequency" name="frequency" defaultValue={campaign.frequency}>
                  <option value="weekly">weekly</option>
                  <option value="monthly">monthly</option>
                </Select>
              </div>
              <div>
                <Label htmlFor="status">Status</Label>
                <Select id="status" name="status" defaultValue={campaign.status}>
                  <option value="not_started">not started</option>
                  <option value="in_progress">in progress</option>
                  <option value="paused">paused</option>
                  <option value="finished">finished</option>
                </Select>
              </div>
              <div className="md:col-span-2">
                <Label htmlFor="progressPercent">Progress</Label>
                <TextInput id="progressPercent" name="progressPercent" type="number" min={0} max={100} defaultValue={campaign.progressPercent} required />
              </div>
              <div className="md:col-span-2">
                <Button type="submit" color="blue">Save</Button>
              </div>
            </form>
          </Card>
        </TabItem>

        <TabItem title="Members">
          <Card className="space-y-4">
            <h2 className="text-xl font-semibold">Members</h2>

            <Table>
              <TableHead>
                <TableRow>
                  <TableHeadCell>User</TableHeadCell>
                  <TableHeadCell>Roles</TableHeadCell>
                  <TableHeadCell>Character</TableHeadCell>
                  <TableHeadCell>Actions</TableHeadCell>
                </TableRow>
              </TableHead>
              <TableBody className="divide-y">
                <TableRow>
                  <TableCell className="text-slate-600">—</TableCell>
                  <TableCell className="text-slate-600">—</TableCell>
                  <TableCell className="text-slate-600">—</TableCell>
                  <TableCell className="text-slate-600">—</TableCell>
                </TableRow>
              </TableBody>
            </Table>

            <form id="campaign-invite-form" action={createInvite.bind(null, campaignId)} className="grid grid-cols-1 gap-4 md:grid-cols-2">
              <div>
                <Label htmlFor="email">Email</Label>
                <TextInput id="email" name="email" type="email" required />
              </div>
              <div className="flex items-end">
                <Button type="submit" color="blue">Invite Member</Button>
              </div>
              <div className="md:col-span-2">
                <Label htmlFor="role-player">Roles</Label>
                <div className="space-y-2 pt-2">
                  <label className="flex items-center gap-2 text-sm"><input id="role-player" type="checkbox" name="roles" value="PLAYER" defaultChecked />PLAYER</label>
                  <label className="flex items-center gap-2 text-sm"><input type="checkbox" name="roles" value="MODERATOR" />MODERATOR</label>
                </div>
              </div>
            </form>

            <div className="space-y-2">
              <h3 className="text-lg font-semibold">Pending invites</h3>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableHeadCell>Email</TableHeadCell>
                    <TableHeadCell>Role</TableHeadCell>
                    <TableHeadCell>Sent at</TableHeadCell>
                    <TableHeadCell>Actions</TableHeadCell>
                  </TableRow>
                </TableHead>
                <TableBody className="divide-y">
                  {pendingInvites.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={4} className="text-slate-600">No pending invites.</TableCell>
                    </TableRow>
                  ) : pendingInvites.map((invite) => (
                    <TableRow key={invite.id}>
                      <TableCell>{invite.email}</TableCell>
                      <TableCell>{invite.roles.join(", ") || "PLAYER"}</TableCell>
                      <TableCell>{new Date(invite.createdAt).toLocaleString()}</TableCell>
                      <TableCell>
                        <form action={cancelInvite.bind(null, campaignId)}>
                          <input type="hidden" name="inviteId" value={invite.id} />
                          <ConfirmSubmitButton
                            label="Cancel"
                            confirmMessage="Cancel this invite?"
                            color="blue"
                            size="xs"
                          />
                        </form>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </Card>
        </TabItem>

        <TabItem title="Sessions">
          <Card className="space-y-4">
            <div className="flex items-center justify-between gap-3">
              <h2 className="text-xl font-semibold">Sessions</h2>
              {canCreateSession ? (
                <Button href={`/app/campaigns/${campaignId}/sessions/new`} color="blue">+ New Session</Button>
              ) : null}
            </div>

            <Table>
              <TableHead>
                <TableRow>
                  <TableHeadCell>Date</TableHeadCell>
                  <TableHeadCell>Title</TableHeadCell>
                  <TableHeadCell>XP Logged</TableHeadCell>
                  <TableHeadCell>Actions</TableHeadCell>
                </TableRow>
              </TableHead>
              <TableBody className="divide-y">
                {sessions.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={4} className="text-slate-600">No sessions yet.</TableCell>
                  </TableRow>
                ) : sessions.map((session) => (
                  <TableRow key={session.id}>
                    <TableCell>{new Date(session.scheduledAt).toLocaleString()}</TableCell>
                    <TableCell>{session.title}</TableCell>
                    <TableCell>—</TableCell>
                    <TableCell>
                      <div className="flex flex-wrap gap-2">
                        <Button
                          href={`/app/campaigns/${campaignId}/sessions/${session.id}/edit`}
                          color="blue"
                          size="xs"
                        >
                          Editar informações da sessão
                        </Button>
                        <Button color="light" size="xs" disabled>
                          Iniciar sessão
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Card>
        </TabItem>
      </Tabs>

      {!isGameMaster ? (
        <Card>
          <h2 className="text-xl font-semibold">My campaign character</h2>
          {!campaignCharacter ? (
            <p className="text-sm text-slate-600">Você ainda não está nesta campanha com personagem (aceite um convite para criar membership).</p>
          ) : (
            <div className="space-y-4">
              <div className="rounded-lg border border-gray-200 p-3 text-sm">
                <p>Level: {campaignCharacter.level}</p>
                <p>XP: {campaignCharacter.xp}</p>
                <p>Locked: {campaignCharacter.locked ? "yes" : "no"}</p>
              </div>

              <form action={updateCampaignCharacter.bind(null, campaignId)} className="space-y-3">
                <Label htmlFor="sheetStateJson">Sheet state JSON</Label>
                <Textarea id="sheetStateJson" name="sheetStateJson" rows={6} defaultValue={campaignCharacter.sheetStateJson || "{}"} />
                <Label htmlFor="inventoryJson">Inventory JSON</Label>
                <Textarea id="inventoryJson" name="inventoryJson" rows={4} defaultValue={campaignCharacter.inventoryJson || "[]"} />
                <Button type="submit" color="blue">Save sheet</Button>
              </form>

              <form action={addXp.bind(null, campaignId)} className="flex flex-wrap items-end gap-3">
                <input type="hidden" name="campaignCharacterId" value={campaignCharacter.id} />
                <div>
                  <Label htmlFor="gainedXp">Gain XP</Label>
                  <TextInput id="gainedXp" name="gainedXp" type="number" min={0} defaultValue={100} required />
                </div>
                <Button type="submit" color="blue">Apply XP + level-up</Button>
              </form>
            </div>
          )}
        </Card>
      ) : null}
    </section>
  );
}
