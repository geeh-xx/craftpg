import { apiFetch } from "@/lib/api-client";
import { SessionRecord } from "@/lib/api-types";
import { Button, Card, Label, TextInput, Textarea } from "flowbite-react";
import { revalidatePath } from "next/cache";
import { notFound, redirect } from "next/navigation";

type Params = Promise<{ campaignId: string; sessionId: string }>;

async function updateSession(campaignId: string, sessionId: string, formData: FormData) {
  "use server";

  await apiFetch<SessionRecord>(`/campaigns/${campaignId}/sessions/${sessionId}`, {
    method: "PUT",
    body: JSON.stringify({
      title: String(formData.get("title") || ""),
      scheduledAt: String(formData.get("scheduledAt") || ""),
      summary: String(formData.get("summary") || ""),
      notes: String(formData.get("notes") || ""),
      attendanceJson: String(formData.get("attendanceJson") || "[]"),
      xpJson: String(formData.get("xpJson") || "[]"),
      npcsJson: String(formData.get("npcsJson") || "[]"),
      mapsJson: String(formData.get("mapsJson") || "[]"),
      treasuresJson: String(formData.get("treasuresJson") || "[]"),
    }),
  });

  revalidatePath(`/app/campaigns/${campaignId}`);
  redirect(`/app/campaigns/${campaignId}`);
}

function toDatetimeLocal(value: string): string {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return "";
  }
  const offsetMs = date.getTimezoneOffset() * 60_000;
  return new Date(date.getTime() - offsetMs).toISOString().slice(0, 16);
}

export default async function EditSessionPage({ params }: { params: Params }) {
  const { campaignId, sessionId } = await params;

  const sessions = await apiFetch<SessionRecord[]>(`/campaigns/${campaignId}/sessions`);
  const session = sessions.find((item) => item.id === sessionId);

  if (!session) {
    notFound();
  }

  return (
    <section className="mx-auto w-full max-w-4xl">
      <Card>
        <form action={updateSession.bind(null, campaignId, sessionId)} className="space-y-5">
          <h1 className="text-5xl font-semibold tracking-tight text-slate-700">Editar Sessão</h1>

          <div className="space-y-2">
            <Label htmlFor="title" className="text-3xl font-semibold text-slate-700">Título</Label>
            <TextInput id="title" name="title" required defaultValue={session.title} sizing="lg" />
          </div>

          <div className="space-y-2">
            <Label htmlFor="scheduledAt" className="text-3xl font-semibold text-slate-700">Data/Hora</Label>
            <TextInput id="scheduledAt" name="scheduledAt" type="datetime-local" required defaultValue={toDatetimeLocal(session.scheduledAt)} sizing="lg" />
          </div>

          <div className="space-y-2">
            <Label htmlFor="summary" className="text-3xl font-semibold text-slate-700">Resumo</Label>
            <Textarea id="summary" name="summary" rows={3} defaultValue={session.summary || ""} />
          </div>

          <div className="space-y-2">
            <Label htmlFor="notes" className="text-3xl font-semibold text-slate-700">Notas</Label>
            <Textarea id="notes" name="notes" rows={3} defaultValue={session.notes || ""} />
          </div>

          <input type="hidden" name="attendanceJson" value="[]" />
          <input type="hidden" name="xpJson" value="[]" />
          <input type="hidden" name="npcsJson" value="[]" />
          <input type="hidden" name="mapsJson" value="[]" />
          <input type="hidden" name="treasuresJson" value="[]" />

          <Button type="submit" className="w-full bg-blue-600 py-3 text-3xl font-semibold text-white hover:bg-blue-700">
            Salvar
          </Button>
        </form>
      </Card>
    </section>
  );
}
