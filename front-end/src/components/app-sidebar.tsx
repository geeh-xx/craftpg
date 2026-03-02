"use client";

import { Sidebar, SidebarItem, SidebarItemGroup, SidebarItems } from "flowbite-react";
import { usePathname } from "next/navigation";

function itemClass(active: boolean) {
  return active
    ? "rounded-xl bg-blue-500/90 text-white hover:bg-blue-500"
    : "rounded-xl text-slate-600 hover:bg-blue-50 hover:text-blue-700";
}

export function AppSidebar() {
  const pathname = usePathname();

  const isDashboard = pathname === "/app";
  const isCampaigns = pathname.startsWith("/app/campaigns");
  const isCharacters = pathname.startsWith("/app/characters");
  const isSettings = pathname.startsWith("/app/settings");

  return (
    <Sidebar className="h-full bg-transparent" theme={{ root: { base: "h-full", inner: "h-full bg-transparent p-0" } }}>
      <SidebarItems>
        <SidebarItemGroup className="space-y-2">
          <SidebarItem href="/app" className={itemClass(isDashboard)}>Dashboard</SidebarItem>
          <SidebarItem href="/app/campaigns" className={itemClass(isCampaigns)}>Campaigns</SidebarItem>
          <SidebarItem href="/app/characters" className={itemClass(isCharacters)}>Characters</SidebarItem>
          <SidebarItem href="/app/settings" className={itemClass(isSettings)}>Settings</SidebarItem>
        </SidebarItemGroup>
      </SidebarItems>
    </Sidebar>
  );
}
