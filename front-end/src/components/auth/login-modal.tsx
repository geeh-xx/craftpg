"use client";

import { Button, Modal, ModalBody, ModalHeader } from "flowbite-react";
import { signIn } from "next-auth/react";
import { useRouter } from "next/navigation";

export function LoginModal() {
  const router = useRouter();

  return (
    <main className="min-h-screen bg-gradient-to-b from-slate-200 via-slate-100 to-sky-100 px-4 py-6 md:py-8">
      <Modal show onClose={() => router.push("/")} size="md" popup dismissible>
        <ModalHeader>Entrar no CraftPG</ModalHeader>
        <ModalBody>
          <div className="space-y-4 py-2">
            <p className="text-sm text-slate-600">Use sua conta Keycloak para acessar campanhas e personagens.</p>
            <Button
              onClick={() => signIn("keycloak", { callbackUrl: "/app" })}
              className="w-full bg-blue-600 text-white hover:bg-blue-700"
            >
              Login / Sign Up
            </Button>
          </div>
        </ModalBody>
      </Modal>
    </main>
  );
}
