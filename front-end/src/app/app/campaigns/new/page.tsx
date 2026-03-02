import { apiFetch } from "@/lib/api-client";
import { Campaign } from "@/lib/api-types";
import { Button, Card, Label, Select, TextInput, Textarea } from "flowbite-react";
import { redirect } from "next/navigation";

async function createCampaign(formData: FormData) {
  "use server";

  const created = await apiFetch<Campaign>("/campaigns", {
    method: "POST",
    body: JSON.stringify({
      title: String(formData.get("title") || ""),
      description: String(formData.get("description") || ""),
      system: String(formData.get("system") || "tormenta20"),
      frequency: String(formData.get("frequency") || "weekly"),
      status: String(formData.get("status") || "not_started"),
      progressPercent: 0,
    }),
  });

  redirect(`/app/campaigns/${created.id}`);
}

export default function CreateCampaignPage() {
  return (
    <section className="mx-auto w-full max-w-4xl">
      <Card>
        <form action={createCampaign} className="space-y-5">
          <h1 className="text-5xl font-semibold tracking-tight text-slate-700">Create Campaign</h1>

          <div className="space-y-2">
            <Label htmlFor="title" className="text-3xl font-semibold text-slate-700">Campaign Name</Label>
            <TextInput id="title" name="title" required placeholder="e.g. Save the King" sizing="lg" />
          </div>

          <div className="space-y-2">
            <Label htmlFor="frequency" className="text-3xl font-semibold text-slate-700">Frequency</Label>
            <Select id="frequency" name="frequency" required sizing="lg">
              <option value="weekly">Weekly</option>
              <option value="monthly">Monthly</option>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="description" className="text-3xl font-semibold text-slate-700">Description <span className="font-normal text-slate-500">(Optional)</span></Label>
            <Textarea id="description" name="description" rows={3} placeholder="Brief campaign synopsis..." />
          </div>

          <input type="hidden" name="status" value="not_started" />
          <div className="space-y-2 pt-2">
            <Label className="text-3xl font-semibold text-slate-700">System</Label>
            <div className="flex flex-wrap gap-3">
              <label className="cursor-pointer">
                <input type="radio" name="system" value="dnd5e" className="peer sr-only" />
                <span className="inline-flex rounded-md border border-slate-300 px-5 py-2 text-slate-700 peer-checked:border-blue-600 peer-checked:bg-blue-600 peer-checked:text-white">D&D 5e</span>
              </label>
              <label className="cursor-pointer">
                <input type="radio" name="system" value="tormenta20" className="peer sr-only" defaultChecked />
                <span className="inline-flex rounded-md border border-slate-300 px-5 py-2 text-slate-700 peer-checked:border-blue-600 peer-checked:bg-blue-600 peer-checked:text-white">Tormenta20</span>
              </label>
              <label className="cursor-pointer">
                <input type="radio" name="system" value="custom" className="peer sr-only" />
                <span className="inline-flex rounded-md border border-slate-300 px-5 py-2 text-slate-700 peer-checked:border-blue-600 peer-checked:bg-blue-600 peer-checked:text-white">Custom</span>
              </label>
            </div>
          </div>

          <Button type="submit" className="w-full bg-blue-600 py-3 text-3xl font-semibold text-white hover:bg-blue-700">Create</Button>
        </form>
      </Card>
    </section>
  );
}
