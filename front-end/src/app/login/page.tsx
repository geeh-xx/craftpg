import { auth } from "@/auth";
import { redirect } from "next/navigation";
import { LoginModal } from "@/components/auth/login-modal";

export default async function LoginPage() {
  const session = await auth();
  if (session) {
    redirect("/app");
  }

  return <LoginModal />;
}
