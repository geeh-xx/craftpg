import { apiFetch } from "@/lib/api-client";
import { SessionRecord } from "@/lib/api-types";
import { Button, Card, Checkbox, Label, TabItem, Tabs, Table, TableBody, TableCell, TableHead, TableHeadCell, TableRow, TextInput, Textarea } from "flowbite-react";
import { redirect } from "next/navigation";

type Params = Promise<{ campaignId: string }>;

async function createSession(campaignId: string, formData: FormData) {
  "use server";

  await apiFetch<SessionRecord>(`/campaigns/${campaignId}/sessions`, {
    method: "POST",
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

  redirect(`/app/campaigns/${campaignId}`);
}

export default async function CreateSessionPage({ params }: { params: Params }) {
  const { campaignId } = await params;

  return (
    <section className="space-y-4">
      <h1 className="text-3xl font-semibold">Create Session</h1>
      <Card>
        <form action={createSession.bind(null, campaignId)} className="space-y-4">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div>
              <Label htmlFor="title">Title</Label>
              <TextInput id="title" name="title" required />
            </div>
            <div>
              <Label htmlFor="scheduledAt">Date/Time</Label>
              <TextInput id="scheduledAt" name="scheduledAt" type="datetime-local" required />
            </div>
            <div className="md:col-span-2">
              <Label htmlFor="summary">Summary</Label>
              <Textarea id="summary" name="summary" rows={3} />
            </div>
            <div className="md:col-span-2">
              <Label htmlFor="notes">Notes</Label>
              <Textarea id="notes" name="notes" rows={3} />
            </div>
          </div>

          <Tabs aria-label="create-session-tabs" variant="underline">
            <TabItem active title="Attendance">
              <div className="space-y-3">
                <Table>
                  <TableHead>
                    <TableHeadCell>Character</TableHeadCell>
                    <TableHeadCell>Present</TableHeadCell>
                    <TableHeadCell>XP gained</TableHeadCell>
                  </TableHead>
                  <TableBody>
                    <TableRow>
                      <TableCell>Character A</TableCell>
                      <TableCell><Checkbox /></TableCell>
                      <TableCell><TextInput type="number" min={0} defaultValue={0} /></TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
                <Label htmlFor="attendanceJson">Attendance JSON</Label>
                <Textarea id="attendanceJson" name="attendanceJson" defaultValue="[]" rows={4} />
                <Label htmlFor="xpJson">XP JSON</Label>
                <Textarea id="xpJson" name="xpJson" defaultValue="[]" rows={4} />
              </div>
            </TabItem>

            <TabItem title="NPCs">
              <Label htmlFor="npcsJson">NPC list (name, notes, stat payload)</Label>
              <Textarea id="npcsJson" name="npcsJson" defaultValue="[]" rows={8} />
            </TabItem>

            <TabItem title="Maps">
              <Label htmlFor="mapsJson">Maps list (title, storage path, notes)</Label>
              <Textarea id="mapsJson" name="mapsJson" defaultValue="[]" rows={8} />
            </TabItem>

            <TabItem title="Treasure">
              <Label htmlFor="treasuresJson">Treasure list (title, json payload)</Label>
              <Textarea id="treasuresJson" name="treasuresJson" defaultValue="[]" rows={8} />
            </TabItem>
          </Tabs>

          <Button type="submit" className="w-full bg-blue-600 py-3 text-3xl font-semibold text-white hover:bg-blue-700">
            Salvar sessão
          </Button>
        </form>
      </Card>
    </section>
  );
}
