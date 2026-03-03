"use client";

import { useMemo, useState } from "react";
import { Button, Label, Select, TextInput } from "flowbite-react";

type ActionFn = (formData: FormData) => void | Promise<void>;

type Props = {
  createAction: ActionFn;
  generateAction: ActionFn;
};

type AttributeKey = "forca" | "destreza" | "constituicao" | "inteligencia" | "sabedoria" | "carisma";
type SkillName =
  | "Acrobacia"
  | "Atletismo"
  | "Conhecimento"
  | "Fortitude"
  | "Furtividade"
  | "Iniciativa"
  | "Intimidacao"
  | "Intuicao"
  | "Investigacao"
  | "Ladinagem"
  | "Luta"
  | "Misticismo"
  | "Percepcao"
  | "Pontaria"
  | "Reflexos"
  | "Vontade";

const ATTRIBUTES: Array<{ key: AttributeKey; label: string }> = [
  { key: "forca", label: "Força" },
  { key: "destreza", label: "Destreza" },
  { key: "constituicao", label: "Constituição" },
  { key: "inteligencia", label: "Inteligência" },
  { key: "sabedoria", label: "Sabedoria" },
  { key: "carisma", label: "Carisma" },
];

const CLASS_BASES: Record<string, Record<AttributeKey, number>> = {
  Guerreiro: { forca: 4, destreza: 2, constituicao: 3, inteligencia: 1, sabedoria: 1, carisma: 1 },
  Paladino: { forca: 3, destreza: 1, constituicao: 2, inteligencia: 1, sabedoria: 2, carisma: 4 },
  Clerigo: { forca: 1, destreza: 1, constituicao: 2, inteligencia: 2, sabedoria: 4, carisma: 2 },
  Arcanista: { forca: 0, destreza: 2, constituicao: 1, inteligencia: 5, sabedoria: 1, carisma: 2 },
  Ladino: { forca: 1, destreza: 5, constituicao: 1, inteligencia: 2, sabedoria: 1, carisma: 2 },
  Barbaro: { forca: 5, destreza: 2, constituicao: 4, inteligencia: 0, sabedoria: 1, carisma: 0 },
};

const RACE_MODIFIERS: Record<string, Record<AttributeKey, number>> = {
  Humano: { forca: 1, destreza: 1, constituicao: 1, inteligencia: 1, sabedoria: 1, carisma: 1 },
  Anao: { forca: 1, destreza: 0, constituicao: 2, inteligencia: 0, sabedoria: 1, carisma: -1 },
  Elfo: { forca: -1, destreza: 2, constituicao: 0, inteligencia: 1, sabedoria: 0, carisma: 1 },
  Qareen: { forca: 0, destreza: 1, constituicao: 0, inteligencia: 1, sabedoria: 0, carisma: 2 },
  Lefou: { forca: 2, destreza: 0, constituicao: 1, inteligencia: 0, sabedoria: 0, carisma: -1 },
  Minotauro: { forca: 2, destreza: 0, constituicao: 2, inteligencia: 0, sabedoria: -1, carisma: 0 },
};

const SKILL_ATTRIBUTE_MAP: Record<SkillName, AttributeKey> = {
  Acrobacia: "destreza",
  Atletismo: "forca",
  Conhecimento: "inteligencia",
  Fortitude: "constituicao",
  Furtividade: "destreza",
  Iniciativa: "destreza",
  Intimidacao: "carisma",
  Intuicao: "sabedoria",
  Investigacao: "inteligencia",
  Ladinagem: "destreza",
  Luta: "forca",
  Misticismo: "inteligencia",
  Percepcao: "sabedoria",
  Pontaria: "destreza",
  Reflexos: "destreza",
  Vontade: "sabedoria",
};

const CLASS_SKILL_BONUS: Record<string, Partial<Record<SkillName, number>>> = {
  Guerreiro: { Luta: 2, Fortitude: 2, Atletismo: 1 },
  Paladino: { Vontade: 2, Luta: 1, Intimidacao: 1 },
  Clerigo: { Vontade: 2, Conhecimento: 1, Intuicao: 1 },
  Arcanista: { Misticismo: 2, Conhecimento: 2, Investigacao: 1 },
  Ladino: { Furtividade: 2, Ladinagem: 2, Reflexos: 1, Acrobacia: 1 },
  Barbaro: { Fortitude: 2, Atletismo: 2, Intimidacao: 1 },
};

const RACE_SKILL_BONUS: Record<string, Partial<Record<SkillName, number>>> = {
  Humano: {},
  Anao: { Fortitude: 1, Conhecimento: 1 },
  Elfo: { Percepcao: 1, Reflexos: 1 },
  Qareen: { Misticismo: 1, Intuicao: 1 },
  Lefou: { Intimidacao: 1 },
  Minotauro: { Intimidacao: 1, Atletismo: 1 },
};

const SKILLS: SkillName[] = Object.keys(SKILL_ATTRIBUTE_MAP) as SkillName[];

function makeZeroModifiers(): Record<AttributeKey, number> {
  return {
    forca: 0,
    destreza: 0,
    constituicao: 0,
    inteligencia: 0,
    sabedoria: 0,
    carisma: 0,
  };
}

function toAttributeModifier(score: number): number {
  return Math.trunc((score - 10) / 2);
}

function formatModifier(score: number): string {
  const modifier = toAttributeModifier(score);
  if (modifier > 0) {
    return `+${modifier}`;
  }
  return String(modifier);
}

function buildDefaultSkills(
  attributes: Record<AttributeKey, number>,
  clazz: string,
  race: string,
): Record<SkillName, number> {
  const classBonus = CLASS_SKILL_BONUS[clazz] ?? {};
  const raceBonus = RACE_SKILL_BONUS[race] ?? {};

  return SKILLS.reduce((accumulator, skillName) => {
    const attributeScore = attributes[SKILL_ATTRIBUTE_MAP[skillName]];
    accumulator[skillName] =
      toAttributeModifier(attributeScore)
      + (classBonus[skillName] ?? 0)
      + (raceBonus[skillName] ?? 0);
    return accumulator;
  }, {} as Record<SkillName, number>);
}

export function CreateCharacterForm({ createAction, generateAction }: Props) {
  const classOptions = Object.keys(CLASS_BASES);
  const raceOptions = Object.keys(RACE_MODIFIERS);

  const [clazz, setClazz] = useState(classOptions[0]);
  const [race, setRace] = useState(raceOptions[0]);
  const [customModifiers, setCustomModifiers] = useState<Record<AttributeKey, number>>(makeZeroModifiers());

  const finalAttributes = useMemo(() => {
    const classBase = CLASS_BASES[clazz];
    const raceBase = RACE_MODIFIERS[race];

    return ATTRIBUTES.reduce((acc, attr) => {
      const key = attr.key;
      acc[key] = classBase[key] + raceBase[key] + customModifiers[key];
      return acc;
    }, {} as Record<AttributeKey, number>);
  }, [clazz, race, customModifiers]);

  const defaultSkills = useMemo(
    () => buildDefaultSkills(finalAttributes, clazz, race),
    [finalAttributes, clazz, race],
  );

  const [skillOverrides, setSkillOverrides] = useState<Partial<Record<SkillName, number>>>({});

  const finalSkills = useMemo(
    () => SKILLS.reduce((accumulator, skillName) => {
      accumulator[skillName] = skillOverrides[skillName] ?? defaultSkills[skillName];
      return accumulator;
    }, {} as Record<SkillName, number>),
    [defaultSkills, skillOverrides],
  );

  const attributesJson = useMemo(
    () => JSON.stringify({
      ruleset: "tormenta20",
      baseClass: clazz,
      baseRace: race,
      attributes: finalAttributes,
      pericias: finalSkills,
      adjustments: customModifiers,
      periciaAjustesManuais: skillOverrides,
    }),
    [clazz, race, finalAttributes, finalSkills, customModifiers, skillOverrides],
  );

  function changeModifier(key: AttributeKey, delta: number) {
    setCustomModifiers((prev) => ({
      ...prev,
      [key]: prev[key] + delta,
    }));
  }

  function setSkillValue(skillName: SkillName, value: string) {
    if (value.trim() === "") {
      setSkillOverrides((previous) => {
        const next = { ...previous };
        delete next[skillName];
        return next;
      });
      return;
    }

    const numericValue = Number(value);
    if (Number.isNaN(numericValue)) {
      return;
    }

    setSkillOverrides((previous) => ({
      ...previous,
      [skillName]: numericValue,
    }));
  }

  return (
    <form action={createAction} className="grid grid-cols-1 gap-3 md:grid-cols-2">
      <div>
        <Label htmlFor="name">Name</Label>
        <TextInput id="name" name="name" required />
      </div>

      <div>
        <Label htmlFor="race">Raça base (Tormenta20)</Label>
        <Select id="race" name="race" value={race} onChange={(event) => setRace(event.target.value)}>
          {raceOptions.map((raceOption) => (
            <option key={raceOption} value={raceOption}>{raceOption}</option>
          ))}
        </Select>
      </div>

      <div>
        <Label htmlFor="clazz">Classe base (Tormenta20)</Label>
        <Select id="clazz" name="clazz" value={clazz} onChange={(event) => setClazz(event.target.value)}>
          {classOptions.map((classOption) => (
            <option key={classOption} value={classOption}>{classOption}</option>
          ))}
        </Select>
      </div>

      <input type="hidden" name="attributesJson" value={attributesJson} />

      <div className="md:col-span-2 rounded-md border border-slate-200 p-3">
        <h3 className="mb-3 text-sm font-semibold text-slate-700">Atributos calculados</h3>
        <div className="grid grid-cols-1 gap-2 md:grid-cols-2">
          {ATTRIBUTES.map((attr) => (
            <div key={attr.key} className="flex items-center justify-between gap-2 rounded border border-slate-200 px-2 py-1.5">
              <span className="text-sm text-slate-700">{attr.label}</span>
              <div className="flex items-center gap-2">
                <Button type="button" size="xs" color="light" onClick={() => changeModifier(attr.key, -1)}>-</Button>
                <div className="min-w-24 text-center text-sm font-semibold">
                  <span>{finalAttributes[attr.key]}</span>
                  <span className="ml-2 text-slate-600">({formatModifier(finalAttributes[attr.key])})</span>
                </div>
                <Button type="button" size="xs" color="light" onClick={() => changeModifier(attr.key, 1)}>+</Button>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="md:col-span-2 rounded-md border border-slate-200 p-3">
        <h3 className="mb-3 text-sm font-semibold text-slate-700">Perícias (padrão por raça/classe + ajuste manual)</h3>
        <div className="grid grid-cols-1 gap-2 md:grid-cols-2">
          {SKILLS.map((skillName) => (
            <div key={skillName} className="rounded border border-slate-200 px-2 py-2">
              <div className="mb-1 flex items-center justify-between">
                <span className="text-sm text-slate-700">{skillName}</span>
                <span className="text-xs text-slate-500">Padrão: {formatModifier(defaultSkills[skillName])}</span>
              </div>
              <TextInput
                type="number"
                sizing="sm"
                value={String(finalSkills[skillName])}
                onChange={(event) => setSkillValue(skillName, event.target.value)}
              />
            </div>
          ))}
        </div>
      </div>

      <div className="md:col-span-2 flex gap-3">
        <Button type="submit" color="blue" className="flex-1">Salvar</Button>
        <Button type="submit" formAction={generateAction} color="light" className="flex-1">Generate random</Button>
      </div>
    </form>
  );
}
