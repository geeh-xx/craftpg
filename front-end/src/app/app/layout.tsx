import { auth } from "@/auth";
import { redirect } from "next/navigation";
import { Avatar, Dropdown, DropdownItem, Navbar, NavbarBrand, TextInput } from "flowbite-react";
import { AppSidebar } from "@/components/app-sidebar";

export default async function AppLayout({ children }: { children: React.ReactNode }) {
  const session = await auth();
  if (!session) {
    redirect("/login");
  }

  const displayName = session.user?.name || "John Doe";
  const displayEmail = session.user?.email || "john@example.com";

  return (
    <main className="min-h-screen bg-gradient-to-b from-blue-50 via-slate-100 to-slate-200 p-4 md:p-6">
      <div className="mx-auto flex min-h-[calc(100vh-2rem)] w-full max-w-[1280px] overflow-hidden rounded-3xl border border-slate-200/80 bg-white/65 shadow-2xl backdrop-blur">
        <aside className="hidden w-64 shrink-0 border-r border-slate-200/80 bg-gradient-to-b from-white/70 to-slate-100/80 lg:flex lg:flex-col">
          <div className="h-[74px] border-b border-slate-200/80 px-6">
            <a href="/app" className="flex h-full items-center gap-2 text-3xl font-semibold text-slate-800">
              <span className="inline-block h-4 w-8 rounded-full bg-gradient-to-r from-blue-400 to-sky-300" />
              Logo
            </a>
          </div>

          <div className="flex-1 px-3 py-5">
            <AppSidebar />
          </div>

          <div className="m-4 flex items-center gap-3 rounded-2xl border border-slate-200/80 bg-white/80 p-3">
            <Avatar rounded size="md" alt="User avatar" />
            <div className="min-w-0">
              <p className="truncate text-sm font-semibold text-slate-700">{displayName}</p>
              <p className="truncate text-xs text-slate-500">{displayEmail}</p>
            </div>
          </div>
        </aside>

        <div className="min-w-0 flex-1">
          <Navbar fluid rounded={false} className="h-[74px] border-b border-slate-200/80 bg-white/75 px-4 backdrop-blur md:px-6">
            <NavbarBrand href="/app" className="lg:hidden">CraftPG</NavbarBrand>
            <div className="hidden max-w-xl flex-1 lg:block">
              <TextInput placeholder="Search campaigns..." sizing="md" className="w-full" />
            </div>
            <div className="ml-auto flex items-center gap-4">
              <span className="text-sm text-slate-600">{displayName}</span>
              <Dropdown arrowIcon={false} inline label={<Avatar rounded size="sm" alt="User avatar" />}>
                <DropdownItem href="/app/settings">Settings</DropdownItem>
                <DropdownItem href="/logout">Sign out</DropdownItem>
              </Dropdown>
            </div>
          </Navbar>

          <section className="p-4 md:p-6">{children}</section>
        </div>
      </div>
    </main>
  );
}
