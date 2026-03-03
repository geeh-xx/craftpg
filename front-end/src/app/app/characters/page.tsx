import { revalidatePath } from "next/cache";
import { Badge, Button, Card, Label, TabItem, Tabs, TextInput } from "flowbite-react";
import { apiFetch } from "@/lib/api-client";
import { CreateCharacterForm } from "@/components/characters/create-character-form";
import { CharacterBase } from "@/lib/api-types";

async function createCharacter(formData: FormData) {
  "use server";
  await apiFetch<CharacterBase>("/characters", {
    method: "POST",
    body: JSON.stringify({
      name: String(formData.get("name") || ""),
      race: String(formData.get("race") || ""),
      clazz: String(formData.get("clazz") || ""),
      attributesJson: String(formData.get("attributesJson") || "{}"),
    }),
  });
  revalidatePath("/app/characters");
}

async function generateRandomCharacter() {
  "use server";
  await apiFetch<CharacterBase>("/characters/generate-random", {
    method: "POST",
  });
  revalidatePath("/app/characters");
}

export default async function CharactersPage() {
  let characters: CharacterBase[] = [];
  try {
    characters = await apiFetch<CharacterBase[]>("/characters");
  } catch {
    characters = [];
  }

  const activeCharacter = characters[0];

  return (
    <section className="space-y-4">
      <div className="flex items-center justify-between gap-3">
        <h1 className="text-3xl font-semibold">Characters</h1>
        <Button>+ New Character</Button>
      </div>

      <div className="rounded-lg border bg-white p-4 shadow-sm">
        <div className="flex flex-wrap items-start justify-between gap-3">
          <div>
            <h2 className="text-2xl font-semibold">Character sheet</h2>
            <p className="text-sm text-slate-600">
              {activeCharacter ? `${activeCharacter.name} · ${activeCharacter.race || "Race"} · ${activeCharacter.clazz || "Class"}` : "No selected character"}
            </p>
          </div>
          <Badge color="warning">Character is locked for this campaign</Badge>
        </div>
      </div>

      <Card>
        <h2 className="text-xl font-semibold">Create character</h2>
        <CreateCharacterForm createAction={createCharacter} generateAction={generateRandomCharacter} />
      </Card>

      {characters.length === 0 ? <Card>Create your first character.</Card> : (
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-3">
          {characters.map((character) => (
            <Card key={character.id}>
              <h2 className="text-xl font-semibold">{character.name}</h2>
              <p className="text-slate-600">Class: {character.clazz || "-"}</p>
              <p className="text-slate-600">Ruleset: Tormenta20</p>
              <Button color="light" href={`/app/characters/${character.id}`}>Open</Button>
              <Tabs aria-label="Character tabs" variant="underline">
                <TabItem active title="Attributes"><pre className="overflow-x-auto text-xs">{character.attributesJson ?? "{}"}</pre></TabItem>
                <TabItem title="Skills">Skills preview.</TabItem>
                <TabItem title="Powers/Features">Powers preview.</TabItem>
                <TabItem title="Spells">Spells preview.</TabItem>
                <TabItem title="Inventory">Inventory preview.</TabItem>
                <TabItem title="Notes">Notes preview.</TabItem>
              </Tabs>
            </Card>
          ))}
        </div>
      )}
    </section>
  );
}
