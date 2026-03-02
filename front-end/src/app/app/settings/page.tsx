import { Badge, Card } from "flowbite-react";

export default function SettingsPage() {
  return (
    <section className="space-y-4">
      <h1 className="text-3xl font-semibold">Settings</h1>
      <Card>
        <h2 className="text-xl font-semibold">Account</h2>
        <p className="text-sm text-slate-600">Profile and preferences settings will be available soon.</p>
        <div className="mt-2">
          <Badge color="info">MVP</Badge>
        </div>
      </Card>
    </section>
  );
}
