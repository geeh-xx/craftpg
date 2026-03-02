import { Card } from "flowbite-react";

export default function AppIndex() {
  return (
    <section className="space-y-4">
      <h1 className="text-3xl font-semibold">Dashboard</h1>
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
        <Card><h2 className="font-semibold">Campaigns</h2><p className="text-sm text-slate-600">Manage your campaigns and progress.</p></Card>
        <Card><h2 className="font-semibold">Characters</h2><p className="text-sm text-slate-600">Access base characters and sheet data.</p></Card>
        <Card><h2 className="font-semibold">Sessions</h2><p className="text-sm text-slate-600">Track sessions, attendance and XP.</p></Card>
      </div>
    </section>
  );
}
