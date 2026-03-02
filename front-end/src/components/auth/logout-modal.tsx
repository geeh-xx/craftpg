"use client";

import { Button, Modal, ModalBody, ModalHeader } from "flowbite-react";
import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";

type LogoutModalProps = {
  issuer?: string;
  clientId?: string;
};

export function LogoutModal({ issuer, clientId }: LogoutModalProps) {
  const router = useRouter();
  const { data: session } = useSession();

  async function handleLogout() {
    await signOut({ redirect: false, callbackUrl: "/" });

    if (!issuer) {
      router.push("/");
      return;
    }

    const endSessionUrl = new URL(`${issuer}/protocol/openid-connect/logout`);
    endSessionUrl.searchParams.set("post_logout_redirect_uri", `${window.location.origin}/`);
    if (clientId) {
      endSessionUrl.searchParams.set("client_id", clientId);
    }
    if (session?.idToken) {
      endSessionUrl.searchParams.set("id_token_hint", session.idToken);
    }

    window.location.assign(endSessionUrl.toString());
  }

  return (
    <main className="min-h-screen bg-gradient-to-b from-slate-200 via-slate-100 to-sky-100 px-4 py-6 md:py-8">
      <Modal show onClose={() => router.push("/app")} size="md" popup dismissible>
        <ModalHeader>Sair da conta</ModalHeader>
        <ModalBody>
          <div className="space-y-4 py-2">
            <p className="text-sm text-slate-600">Você será redirecionado para a landing page.</p>
            <Button className="w-full border border-slate-300 bg-white text-slate-700 hover:bg-slate-100" onClick={() => router.push("/app")}>Cancelar</Button>
            <Button className="w-full bg-red-600 text-white hover:bg-red-700" onClick={handleLogout}>Logout</Button>
          </div>
        </ModalBody>
      </Modal>
    </main>
  );
}
