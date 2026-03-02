import { auth } from "@/auth";
import { headers } from "next/headers";

async function resolveBearerToken(): Promise<string | undefined> {
  const session = await auth();
  if (session?.error) {
    return undefined;
  }

  if (session?.accessTokenExpires && Date.now() >= session.accessTokenExpires - 5_000) {
    return undefined;
  }

  const directToken = session?.accessToken ?? session?.idToken;
  if (directToken) {
    return directToken;
  }

  const requestHeaders = await headers();
  const cookie = requestHeaders.get("cookie");
  if (!cookie) {
    return undefined;
  }

  const authBaseUrl = process.env.NEXTAUTH_URL ?? process.env.AUTH_URL ?? "http://localhost:3000";
  try {
    const sessionRes = await fetch(`${authBaseUrl}/api/auth/session`, {
      headers: { cookie },
      cache: "no-store",
    });
    if (!sessionRes.ok) {
      return undefined;
    }
    const sessionJson = (await sessionRes.json()) as { accessToken?: string; idToken?: string; error?: string; accessTokenExpires?: number };
    if (sessionJson.error) {
      return undefined;
    }
    if (sessionJson.accessTokenExpires && Date.now() >= sessionJson.accessTokenExpires - 5_000) {
      return undefined;
    }
    return sessionJson.accessToken ?? sessionJson.idToken;
  } catch {
    return undefined;
  }
}

export async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  const bearerToken = await resolveBearerToken();
  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${path}`, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers || {}),
      ...(bearerToken ? { Authorization: `Bearer ${bearerToken}` } : {}),
    },
    cache: "no-store",
  });

  if (res.status === 401) {
    throw new Error(`unauthorized (${bearerToken ? "token-present" : "token-missing"})`);
  }
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || "api error");
  }

  if (res.status === 204) {
    return undefined as T;
  }

  const text = await res.text();
  if (!text) {
    return undefined as T;
  }
  return JSON.parse(text) as T;
}

export async function apiFetchVoid(path: string, init?: RequestInit): Promise<void> {
  await apiFetch<void>(path, init);
}
