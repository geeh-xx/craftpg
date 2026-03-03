"use client";

import { useEffect } from "react";
import { signIn } from "next-auth/react";

export default function LoginPage() {
  useEffect(() => {
    void signIn("keycloak", { callbackUrl: "/app" });
  }, []);

  return null;
}
