import { Alert, Button, Card, Progress, Spinner } from "flowbite-react";
import { apiFetch } from "@/lib/api-client";
import { Campaign } from "@/lib/api-types";
import { redirect } from "next/navigation";

function statusLabel(status: string) {
  if (status === "in_progress") return "in progress";
  if (status === "not_started") return "not started";
  if (status === "paused") return "paused";
  if (status === "finished") return "finished";
  return status;
}

function progressColor(status: string) {
  if (status === "finished") return "green" as const;
  return "blue" as const;
}

export default async function CampaignsPage() {
  let campaigns: Campaign[] = [];
  let hasError = false;
  let errorMessage = "Error loading campaigns.";
  try {
    campaigns = await apiFetch<Campaign[]>("/campaigns");
  } catch (error) {
    const message = error instanceof Error ? error.message : "api error";
    if (message === "unauthorized") {
      redirect("/login");
    }
    errorMessage = message || errorMessage;
    hasError = true;
  }

  return (
    <section className="space-y-5">
      <div className="flex items-center justify-between gap-3 border-b border-slate-200 pb-4">
        <h1 className="text-5xl font-semibold tracking-tight text-slate-700">Campaigns</h1>
        <Button href="/app/campaigns/new" color="blue" size="lg" pill>+ New Campaign</Button>
      </div>

      {!hasError && campaigns.length === 0 ? (
        <Card className="text-center">
          <div className="mx-auto flex w-full max-w-md flex-col items-center gap-3 py-4">
            <p className="text-sm text-slate-600">Create your first campaign</p>
            <Button href="/app/campaigns/new" color="blue">Create campaign</Button>
          </div>
        </Card>
      ) : null}

      {hasError ? <Alert color="failure">Error loading campaigns: {errorMessage}</Alert> : null}

      {!hasError && campaigns.length > 0 ? (
        <div className="space-y-4">
          {campaigns.map((campaign) => (
            <Card key={campaign.id} className="overflow-hidden border border-slate-200/90 bg-white/80 p-0">
              <div className="flex min-h-48 flex-col md:flex-row">
                <div className="flex-1 space-y-4 px-6 py-5">
                  <h2 className="line-clamp-1 text-5xl font-semibold text-slate-700">{campaign.title}</h2>
                  <p className="text-3xl text-slate-500">
                    {campaign.frequency} · {statusLabel(campaign.status)} · — Sessions · {campaign.progressPercent}%
                  </p>
                  <div className="max-w-xl">
                    <Progress progress={campaign.progressPercent} color={progressColor(campaign.status)} size="lg" />
                  </div>
                </div>

                <div className="relative min-h-40 w-full md:min-h-full md:w-[36%]">
                  <div className="absolute inset-0 bg-gradient-to-l from-blue-200 via-slate-100 to-slate-50" />
                  <div className="absolute inset-0 bg-[radial-gradient(circle_at_75%_45%,rgba(59,130,246,0.28),transparent_45%)]" />
                  <div className="absolute bottom-0 right-0 h-full w-full bg-[linear-gradient(135deg,transparent_28%,rgba(148,163,184,0.18)_100%)]" />
                </div>
              </div>
              <div className="px-6 pb-5">
                <Button href={`/app/campaigns/${campaign.id}`} color="light" pill>Open</Button>
              </div>
            </Card>
          ))}
        </div>
      ) : null}

      {!hasError && campaigns.length === 0 ? <div className="hidden"><Spinner /></div> : null}
    </section>
  );
}
