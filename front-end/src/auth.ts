import NextAuth from "next-auth";
import Keycloak from "next-auth/providers/keycloak";

const authSecret = process.env.AUTH_SECRET ?? process.env.NEXTAUTH_SECRET;

type TokenShape = {
  accessToken?: string;
  idToken?: string;
  refreshToken?: string;
  accessTokenExpires?: number;
  error?: string;
};

function decodeJwtExpMs(token?: string): number | undefined {
  if (!token) {
    return undefined;
  }

  const parts = token.split(".");
  if (parts.length < 2) {
    return undefined;
  }

  try {
    const payload = JSON.parse(Buffer.from(parts[1], "base64url").toString("utf-8")) as { exp?: number };
    if (!payload.exp || Number.isNaN(payload.exp)) {
      return undefined;
    }
    return payload.exp * 1000;
  } catch {
    return undefined;
  }
}

async function refreshAccessToken(token: TokenShape): Promise<TokenShape> {
  if (!token.refreshToken) {
    return { ...token, error: "NoRefreshToken" };
  }

  try {
    const issuer = process.env.KEYCLOAK_ISSUER;
    const clientId = process.env.KEYCLOAK_CLIENT_ID;
    const clientSecret = process.env.KEYCLOAK_CLIENT_SECRET;

    if (!issuer || !clientId) {
      return { ...token, error: "MissingKeycloakConfig" };
    }

    const params = new URLSearchParams({
      grant_type: "refresh_token",
      client_id: clientId,
      refresh_token: token.refreshToken,
    });

    if (clientSecret) {
      params.set("client_secret", clientSecret);
    }

    const response = await fetch(`${issuer}/protocol/openid-connect/token`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: params.toString(),
    });

    const refreshed = await response.json();
    if (!response.ok) {
      return {
        ...token,
        accessToken: undefined,
        idToken: undefined,
        accessTokenExpires: undefined,
        error: "RefreshAccessTokenError",
      };
    }

    const refreshedAccessToken = refreshed.access_token ?? token.accessToken;
    const refreshedIdToken = refreshed.id_token ?? token.idToken;
    const refreshedAccessTokenExpires = refreshed.expires_in
      ? Date.now() + refreshed.expires_in * 1000
      : decodeJwtExpMs(refreshedAccessToken) ?? token.accessTokenExpires;

    return {
      ...token,
      accessToken: refreshedAccessToken,
      idToken: refreshedIdToken,
      refreshToken: refreshed.refresh_token ?? token.refreshToken,
      accessTokenExpires: refreshedAccessTokenExpires,
      error: undefined,
    };
  } catch {
    return {
      ...token,
      accessToken: undefined,
      idToken: undefined,
      accessTokenExpires: undefined,
      error: "RefreshAccessTokenError",
    };
  }
}

export const { auth, handlers, signIn, signOut } = NextAuth({
  secret: authSecret,
  providers: [
    Keycloak({
      issuer: process.env.KEYCLOAK_ISSUER,
      clientId: process.env.KEYCLOAK_CLIENT_ID,
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET || undefined,
    }),
  ],
  session: { strategy: "jwt" },
  callbacks: {
    async jwt({ token, account }) {
      const mappedToken = token as TokenShape;

      if (account?.access_token) {
        mappedToken.accessToken = account.access_token;
      }
      if (account?.id_token) {
        mappedToken.idToken = account.id_token;
      }
      if (account?.refresh_token) {
        mappedToken.refreshToken = account.refresh_token;
      }
      if (account?.expires_at) {
        mappedToken.accessTokenExpires = account.expires_at * 1000;
      }

      if (!mappedToken.accessTokenExpires) {
        mappedToken.accessTokenExpires = decodeJwtExpMs(mappedToken.accessToken);
      }

      if (mappedToken.accessToken && mappedToken.accessTokenExpires && Date.now() < mappedToken.accessTokenExpires - 10_000) {
        return mappedToken;
      }

      if (mappedToken.refreshToken) {
        return refreshAccessToken(mappedToken);
      }

      return {
        ...mappedToken,
        accessToken: undefined,
        idToken: undefined,
        accessTokenExpires: undefined,
        error: mappedToken.error ?? "TokenExpired",
      };
    },
    async session({ session, token }) {
      const mappedToken = token as TokenShape;
      session.accessToken = mappedToken.accessToken;
      session.idToken = mappedToken.idToken;
      session.accessTokenExpires = mappedToken.accessTokenExpires;
      session.error = mappedToken.error;
      return session;
    },
  },
  pages: {
    signOut: "/logout",
  },
});
