import { apiFetch } from "@/lib/api-client";
import { MeProfile } from "@/lib/api-types";
import { Alert, Button, Card, Label, TextInput } from "flowbite-react";
import { redirect } from "next/navigation";

async function updateProfile(formData: FormData) {
  "use server";

  const displayName = String(formData.get("displayName") || "").trim();
  if (!displayName) {
    redirect("/app/settings?error=display-name");
  }

  try {
    await apiFetch<MeProfile>("/me", {
      method: "PUT",
      body: JSON.stringify({ displayName }),
    });
  } catch {
    redirect("/app/settings?error=save");
  }

  redirect("/app/settings?saved=1");
}

export default async function SettingsPage({
  searchParams,
}: {
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
}) {
  const profile = await apiFetch<MeProfile>("/me");
  const params = (await searchParams) ?? {};
  const saved = params.saved === "1";
  const hasError = typeof params.error === "string";

  return (
    <section className="mx-auto w-full max-w-2xl space-y-4">
      <h1 className="text-3xl font-semibold">Settings</h1>
      <Card>
        <h2 className="text-xl font-semibold">Account</h2>

        {saved ? <Alert color="success">Profile updated successfully.</Alert> : null}
        {hasError ? <Alert color="failure">Could not update profile. Please try again.</Alert> : null}

        <form action={updateProfile} className="mt-4 space-y-4">
          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <TextInput id="email" type="email" value={profile.email} readOnly />
          </div>

          <div className="space-y-2">
            <Label htmlFor="displayName">Display name</Label>
            <TextInput id="displayName" name="displayName" defaultValue={profile.displayName} required />
          </div>

          <Button type="submit" className="w-full bg-blue-600 text-white hover:bg-blue-700">Save</Button>
        </form>
      </Card>
    </section>
  );
}
