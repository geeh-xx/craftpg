import { auth } from "@/auth";
import { redirect } from "next/navigation";
import { LogoutModal } from "@/components/auth/logout-modal";

export default async function LogoutPage() {
  const session = await auth();
  if (!session) {
    redirect("/");
  }

  return <LogoutModal issuer={process.env.KEYCLOAK_ISSUER} clientId={process.env.KEYCLOAK_CLIENT_ID} />;
}
