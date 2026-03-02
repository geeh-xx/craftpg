import { auth } from "@/auth";
import { Alert, Button, Card, Label, Select, TabItem, Tabs, TextInput } from "flowbite-react";
import { apiFetch } from "@/lib/api-client";
import { redirect } from "next/navigation";

type InvitePreview = {
  campaignId: string;
  campaignTitle: string;
  dmName: string;
  email: string;
  roles: string[];
  expiresAt: string;
  accepted: boolean;
};

type Character = {
  id: string;
  name: string;
  race?: string;
  clazz?: string;
};

async function acceptInvite(token: string, characterBaseId: string) {
  "use server";
  const response = await apiFetch<{ campaignId: string }>(`/invites/${token}/accept`, {
    method: "POST",
    body: JSON.stringify({ characterBaseId }),
  });
  redirect(`/app/campaigns/${response.campaignId}`);
}

export default async function InvitePage({ params }: { params: Promise<{ token: string }> }) {
  const { token } = await params;
  const session = await auth();

  const preview = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/invites/${token}`, { cache: "no-store" }).then((r) => r.json()) as InvitePreview;

  let characters: Character[] = [];
  if (session) {
    try {
      characters = await apiFetch<Character[]>("/characters");
    } catch {
      characters = [];
    }
  }

  return (
    <main className="mx-auto max-w-2xl px-4 py-10">
      <div className="space-y-4">
        <Card>
          <h1 className="text-2xl font-semibold">Invite Acceptance</h1>
          <p>Campaign: {preview.campaignTitle}</p>
          <p>DM: {preview.dmName}</p>
          <p>Roles assigned: {preview.roles.join(", ")}</p>
          <p>Expiration date: {new Date(preview.expiresAt).toLocaleString()}</p>
        </Card>

        {!session ? (
          <Alert color="warning">Please login to accept this invite.</Alert>
        ) : null}

        {!session ? (
          <Button href="/login">Sign in to accept</Button>
        ) : (
          <Card>
            <h2 className="mb-3 text-xl font-semibold">Character selection</h2>
            {characters.length === 0 ? (
              <Alert color="info">No characters found. Create a new one or generate random to continue.</Alert>
            ) : null}

            <Tabs aria-label="invite-accept-tabs" variant="underline">
            <TabItem active title="Usar existente">
              <form action={async (formData) => {
                "use server";
                const characterBaseId = String(formData.get("characterBaseId") || "");
                await acceptInvite(token, characterBaseId);
              }} className="space-y-3">
                <Label htmlFor="characterBaseId">Select character</Label>
                <Select id="characterBaseId" name="characterBaseId" required disabled={characters.length === 0}>
                  <option value="">Selecione...</option>
                  {characters.map((character) => (
                    <option key={character.id} value={character.id}>{character.name} {character.race ? `(${character.race} · ${character.clazz || ""})` : ""}</option>
                  ))}
                </Select>
                <Button type="submit" disabled={characters.length === 0}>Accept invite</Button>
              </form>
            </TabItem>

            <TabItem title="Criar manualmente">
              <form action={async (formData) => {
                "use server";
                const created = await apiFetch<Character>("/characters", {
                  method: "POST",
                  body: JSON.stringify({
                    name: String(formData.get("name") || ""),
                    race: String(formData.get("race") || ""),
                    clazz: String(formData.get("clazz") || ""),
                    attributesJson: String(formData.get("attributesJson") || "{}"),
                  }),
                });
                await acceptInvite(token, created.id);
              }} className="space-y-3">
                <div>
                  <Label htmlFor="name">Nome</Label>
                  <TextInput id="name" name="name" required />
                </div>
                <div>
                  <Label htmlFor="race">Raça</Label>
                  <TextInput id="race" name="race" />
                </div>
                <div>
                  <Label htmlFor="clazz">Classe</Label>
                  <TextInput id="clazz" name="clazz" />
                </div>
                <div>
                  <Label htmlFor="attributesJson">Atributos (JSON)</Label>
                  <TextInput id="attributesJson" name="attributesJson" defaultValue="{}" />
                </div>
                <Button type="submit">Criar e aceitar convite</Button>
              </form>
            </TabItem>

            <TabItem title="Gerar aleatório">
              <form action={async () => {
                "use server";
                const randomCharacter = await apiFetch<Character>("/characters/generate-random", {
                  method: "POST",
                });
                await acceptInvite(token, randomCharacter.id);
              }} className="space-y-3">
                <p className="text-sm text-slate-600">Gera um personagem base automaticamente e usa no aceite do convite.</p>
                <Button type="submit" color="light">Gerar e aceitar</Button>
              </form>
            </TabItem>
            </Tabs>
          </Card>
        )}
      </div>
    </main>
  );
}
