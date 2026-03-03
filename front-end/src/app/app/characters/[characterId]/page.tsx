import { Button, Card } from "flowbite-react";
import { notFound } from "next/navigation";
import { apiFetch } from "@/lib/api-client";
import { Campaign, CampaignCharacter, CharacterBase } from "@/lib/api-types";

type Params = Promise<{ characterId: string }>;

type AttributeKey = "forca" | "destreza" | "constituicao" | "inteligencia" | "sabedoria" | "carisma";

const SKILL_ATTRIBUTE_MAP: Record<string, AttributeKey> = {
  Acrobacia: "destreza",
  Adestramento: "carisma",
  Atletismo: "forca",
  Atuacao: "carisma",
  Cavalgar: "destreza",
  Conhecimento: "inteligencia",
  Cura: "sabedoria",
  Diplomacia: "carisma",
  Enganacao: "carisma",
  Fortitude: "constituicao",
  Furtividade: "destreza",
  Guerra: "inteligencia",
  Iniciativa: "destreza",
  Intimidacao: "carisma",
  Intuicao: "sabedoria",
  Investigacao: "inteligencia",
  Jogatina: "carisma",
  Ladinagem: "destreza",
  Luta: "forca",
  Misticismo: "inteligencia",
  Nobreza: "inteligencia",
  Oficio: "inteligencia",
  Percepcao: "sabedoria",
  Pilotagem: "destreza",
  Pontaria: "destreza",
  Reflexos: "destreza",
  Religiao: "sabedoria",
  Sobrevivencia: "sabedoria",
  Vontade: "sabedoria",
};

function toAttributeModifier(score: number): number {
  return Math.trunc((score - 10) / 2);
}

function formatSigned(value: number): string {
  if (value > 0) {
    return `+${value}`;
  }
  return String(value);
}

function buildSkills(attributesJson?: string): Array<{ name: string; modifier: number }> {
  if (!attributesJson || !attributesJson.trim()) {
    return [];
  }

  try {
    const parsed = JSON.parse(attributesJson) as {
      pericias?: Record<string, unknown>;
      attributes?: Partial<Record<AttributeKey, number>>;
    };

    if (parsed.pericias && typeof parsed.pericias === "object") {
      return Object.entries(parsed.pericias)
        .map(([name, value]) => {
          if (typeof value === "number") {
            return { name, modifier: value };
          }
          if (typeof value === "string") {
            const numeric = Number(value);
            if (!Number.isNaN(numeric)) {
              return { name, modifier: numeric };
            }
          }
          if (value && typeof value === "object" && "modifier" in value) {
            const modifier = Number((value as { modifier?: unknown }).modifier);
            if (!Number.isNaN(modifier)) {
              return { name, modifier };
            }
          }
          return null;
        })
        .filter((item): item is { name: string; modifier: number } => item !== null)
        .sort((a, b) => a.name.localeCompare(b.name));
    }

    if (parsed.attributes && typeof parsed.attributes === "object") {
      return Object.entries(SKILL_ATTRIBUTE_MAP)
        .map(([name, attributeKey]) => {
          const score = Number(parsed.attributes?.[attributeKey] ?? 10);
          return { name, modifier: toAttributeModifier(score) };
        })
        .sort((a, b) => a.name.localeCompare(b.name));
    }
  } catch {
    return [];
  }

  return [];
}

function formatAttributes(attributesJson?: string): string {
  if (!attributesJson || !attributesJson.trim()) {
    return "{}";
  }

  try {
    return JSON.stringify(JSON.parse(attributesJson), null, 2);
  } catch {
    return attributesJson;
  }
}

export default async function CharacterDetailPage({ params }: { params: Params }) {
  const { characterId } = await params;

  const characters = await apiFetch<CharacterBase[]>("/characters");
  const character = characters.find((item) => item.id === characterId);

  if (!character) {
    notFound();
  }

  const skills = buildSkills(character.attributesJson);

  let campaigns: Campaign[] = [];
  try {
    campaigns = await apiFetch<Campaign[]>("/campaigns");
  } catch {
    campaigns = [];
  }

  const characterCampaigns: Campaign[] = [];
  for (const campaign of campaigns) {
    try {
      const campaignCharacter = await apiFetch<CampaignCharacter>(`/campaigns/${campaign.id}/characters/me`);
      if (campaignCharacter.characterBaseId === character.id) {
        characterCampaigns.push(campaign);
      }
    } catch {
      continue;
    }
  }

  return (
    <section className="mx-auto w-full max-w-4xl space-y-4">
      <div className="flex items-center justify-between gap-3">
        <h1 className="text-3xl font-semibold">Character details</h1>
        <Button href="/app/characters" color="light">Back</Button>
      </div>

      <Card className="space-y-4">
        <div className="space-y-2">
          <h2 className="text-xl font-semibold">{character.name}</h2>
          <p className="text-sm text-slate-600">Race: {character.race || "-"}</p>
          <p className="text-sm text-slate-600">Class: {character.clazz || "-"}</p>
        </div>

        <div>
          <h3 className="mb-2 text-sm font-semibold text-slate-700">Campanhas cadastradas</h3>
          {characterCampaigns.length === 0 ? (
            <p className="text-sm text-slate-600">Este personagem ainda não está vinculado a campanhas.</p>
          ) : (
            <ul className="space-y-1">
              {characterCampaigns.map((campaign) => (
                <li key={campaign.id} className="text-sm text-slate-700">
                  {campaign.title}
                </li>
              ))}
            </ul>
          )}
        </div>

        <div>
          <h3 className="mb-2 text-sm font-semibold text-slate-700">Perícias</h3>
          {skills.length === 0 ? (
            <p className="text-sm text-slate-600">Sem perícias disponíveis para este personagem.</p>
          ) : (
            <ul className="grid grid-cols-1 gap-1 md:grid-cols-2">
              {skills.map((skill) => (
                <li key={skill.name} className="flex items-center justify-between rounded border border-slate-200 px-2 py-1 text-sm text-slate-700">
                  <span>{skill.name}</span>
                  <span className="font-semibold">{formatSigned(skill.modifier)}</span>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div>
          <h3 className="mb-2 text-sm font-semibold text-slate-700">Attributes (read-only)</h3>
          <pre className="overflow-x-auto rounded border border-slate-200 bg-slate-50 p-3 text-xs">
            {formatAttributes(character.attributesJson)}
          </pre>
        </div>
      </Card>
    </section>
  );
}
