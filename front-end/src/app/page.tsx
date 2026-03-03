import { Button, Card, Navbar, NavbarBrand, NavbarCollapse, NavbarLink, NavbarToggle, TabItem, Tabs } from "flowbite-react";
import { auth } from "@/auth";
import { redirect } from "next/navigation";

export default async function LandingPage() {
  const session = await auth();
  if (session) {
    redirect("/app");
  }

  return (
    <main className="min-h-screen bg-gradient-to-b from-slate-200 via-slate-100 to-sky-100 px-4 py-6 text-slate-800 md:py-8">
      <div className="mx-auto max-w-6xl overflow-hidden rounded-3xl border border-slate-300/70 bg-white/70 shadow-xl backdrop-blur-sm">
        <Navbar fluid rounded={false} className="border-b border-slate-300/80 bg-white/70 px-4 md:px-6">
          <NavbarBrand href="/">
            <span className="self-center whitespace-nowrap text-2xl font-semibold text-slate-700">Logo</span>
          </NavbarBrand>
          <NavbarToggle />
          <NavbarCollapse className="items-center md:gap-2">
            <NavbarLink href="#product" className="inline-flex h-10 items-center text-slate-600">Product</NavbarLink>
            <NavbarLink href="#how" className="inline-flex h-10 items-center text-slate-600">How It Works</NavbarLink>
            <NavbarLink href="#pricing" className="inline-flex h-10 items-center text-slate-600">Pricing</NavbarLink>
            <Button href="/login" className="inline-flex h-10 items-center bg-blue-600 px-4 font-semibold text-white hover:bg-blue-700">Login</Button>
            <Button href="/register" className="inline-flex h-10 items-center bg-blue-600 px-4 font-semibold text-white hover:bg-blue-700">Sign Up</Button>
          </NavbarCollapse>
        </Navbar>

        <section id="product" className="relative grid grid-cols-1 gap-8 border-b border-slate-300/70 px-6 py-10 md:grid-cols-2 md:px-10">
          <div className="space-y-5">
            <h1 className="text-5xl font-bold leading-tight text-slate-800">Adventure Awaits!</h1>
            <p className="max-w-md text-4xl leading-snug text-slate-600">Manage your campaigns and characters online.</p>
            <div className="flex flex-wrap gap-3">
              <Button href="/app/campaigns" size="lg" className="bg-blue-600 hover:bg-blue-700">Create Campaign</Button>
              <Button href="/app/characters" color="light" size="lg" className="border-slate-300 text-slate-700">View Sheet</Button>
            </div>
          </div>
          <Card className="flex min-h-72 items-center justify-center rounded-2xl border border-slate-300/80 bg-gradient-to-br from-slate-100 to-slate-200 shadow-md">
            <div className="w-full space-y-3 px-5 py-4">
              <div className="h-3 w-40 rounded bg-slate-300" />
              <div className="h-3 w-full rounded bg-slate-300" />
              <div className="grid grid-cols-2 gap-4 pt-2">
                <div className="space-y-2">
                  <div className="h-2 w-full rounded bg-slate-300" />
                  <div className="h-2 w-10/12 rounded bg-slate-300" />
                  <div className="h-2 w-8/12 rounded bg-slate-300" />
                  <div className="h-2 w-9/12 rounded bg-slate-300" />
                </div>
                <div className="space-y-2">
                  <div className="h-2 w-full rounded bg-slate-300" />
                  <div className="h-2 w-10/12 rounded bg-slate-300" />
                  <div className="h-2 w-9/12 rounded bg-slate-300" />
                  <div className="h-2 w-7/12 rounded bg-slate-300" />
                </div>
              </div>
            </div>
          </Card>
        </section>

        <section id="how" className="border-b border-slate-300/70 px-6 py-8 md:px-10">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
            <Card className="rounded-2xl border border-slate-200 bg-white/90 text-center shadow-sm">
              <h5 className="text-3xl font-semibold text-slate-800">Campaigns</h5>
              <p className="text-sm text-slate-600">Create and manage your campaigns online.</p>
            </Card>
            <Card className="rounded-2xl border border-slate-200 bg-white/90 text-center shadow-sm">
              <h5 className="text-3xl font-semibold text-slate-800">Sessions</h5>
              <p className="text-sm text-slate-600">Log every session and campaign progress.</p>
            </Card>
            <Card className="rounded-2xl border border-slate-200 bg-white/90 text-center shadow-sm">
              <h5 className="text-3xl font-semibold text-slate-800">Characters</h5>
              <p className="text-sm text-slate-600">Keep each character sheet always updated.</p>
            </Card>
          </div>
        </section>

        <section className="border-b border-slate-300/70 px-6 py-8 md:px-10">
          <h2 className="mb-5 text-center text-4xl font-semibold text-slate-800">Invite Friends</h2>
          <div className="grid grid-cols-1 gap-2 rounded-xl border border-slate-300 bg-white/80 p-2 md:grid-cols-3">
            <div className="rounded-md bg-blue-600 px-4 py-3 text-center text-sm font-semibold text-white">✉ Send Invite</div>
            <div className="rounded-md bg-slate-200 px-4 py-3 text-center text-sm font-semibold text-slate-700">✓ Friend Accepts</div>
            <div className="rounded-md bg-slate-200 px-4 py-3 text-center text-sm font-semibold text-slate-700">3 Join with Character</div>
          </div>
        </section>

        <section className="border-b border-slate-300/70 px-6 py-8 md:px-10">
          <h2 id="faq" className="mb-4 text-center text-4xl font-semibold text-slate-800">FAQ</h2>
          <div className="mb-4 h-28 rounded-xl bg-gradient-to-r from-sky-100 via-slate-100 to-blue-100" />

          <Card className="overflow-hidden rounded-2xl border border-slate-300/70 bg-white/90 p-1 shadow-md">
            <Tabs aria-label="Sheet tabs" variant="underline">
              <TabItem active title="Attributes">
                <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                  <div className="rounded-lg border border-slate-200 bg-slate-50 p-3">
                    <p className="mb-2 text-sm font-semibold text-slate-700">Sheet Overview</p>
                    <div className="space-y-2 text-sm text-slate-700">
                      <div className="flex justify-between border-b border-slate-200 pb-1"><span>STR</span><span>18</span></div>
                      <div className="flex justify-between border-b border-slate-200 pb-1"><span>DEX</span><span>14</span></div>
                      <div className="flex justify-between border-b border-slate-200 pb-1"><span>CON</span><span>16</span></div>
                      <div className="flex justify-between border-b border-slate-200 pb-1"><span>INT</span><span>10</span></div>
                      <div className="flex justify-between"><span>WIS</span><span>12</span></div>
                    </div>
                  </div>
                  <div className="rounded-lg border border-slate-200 bg-slate-50 p-3">
                    <p className="mb-2 text-sm font-semibold text-slate-700">Quick Summary</p>
                    <div className="space-y-2">
                      <div className="h-2 rounded bg-slate-300" />
                      <div className="h-2 rounded bg-slate-300" />
                      <div className="h-2 w-3/4 rounded bg-slate-300" />
                      <div className="h-9 rounded border border-slate-200 bg-slate-100" />
                    </div>
                  </div>
                </div>
              </TabItem>
              <TabItem title="Skills">Skills preview</TabItem>
              <TabItem title="Powers">Powers preview</TabItem>
              <TabItem title="Inventory">Inventory preview</TabItem>
            </Tabs>
          </Card>
        </section>

        <section id="pricing" className="px-6 py-10 text-center md:px-10">
          <h2 className="text-5xl font-semibold text-slate-800">Get Started Today!</h2>
          <div className="mt-4 flex flex-wrap justify-center gap-3">
            <Button href="/register" size="lg" className="bg-blue-600 hover:bg-blue-700">Sign Up</Button>
            <Button href="#how" color="light" size="lg" className="border-slate-300 text-slate-700">Learn More</Button>
          </div>
        </section>
      </div>
    </main>
  );
}
