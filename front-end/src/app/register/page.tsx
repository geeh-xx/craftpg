"use client";

import { useEffect } from "react";
import { signIn } from "next-auth/react";

export default function RegisterPage() {
  useEffect(() => {
    void signIn("keycloak", { callbackUrl: "/app" }, { kc_action: "register" });
  }, []);

  return null;
}
