"use client";

import { Button, Modal, ModalBody, ModalFooter } from "flowbite-react";
import { useState, type MouseEvent } from "react";

type ConfirmSubmitButtonProps = {
  label: string;
  confirmMessage: string;
  color?: "failure" | "info" | "light" | "success" | "warning" | "blue" | "dark" | "gray" | "green" | "lime" | "pink" | "purple" | "red" | "teal" | "yellow";
  size?: "xs" | "sm" | "md" | "lg" | "xl";
};

export function ConfirmSubmitButton({ label, confirmMessage, color = "failure", size = "xs" }: ConfirmSubmitButtonProps) {
  const [isOpen, setIsOpen] = useState(false);
  const [targetForm, setTargetForm] = useState<HTMLFormElement | null>(null);

  function handleClick(event: MouseEvent<HTMLButtonElement>) {
    const form = event.currentTarget.closest("form");
    setTargetForm(form);
    setIsOpen(true);
  }

  function handleConfirm() {
    targetForm?.requestSubmit();
    setIsOpen(false);
    setTargetForm(null);
  }

  function handleClose() {
    setIsOpen(false);
    setTargetForm(null);
  }

  return (
    <>
      <Button type="button" color={color} size={size} onClick={handleClick}>
        {label}
      </Button>

      <Modal show={isOpen} size="md" popup onClose={handleClose}>
        <ModalBody>
          <div className="space-y-4 pt-4 text-center">
            <h3 className="text-lg font-semibold text-gray-900">Confirm action</h3>
            <p className="text-sm text-gray-600">{confirmMessage}</p>
          </div>
        </ModalBody>
        <ModalFooter className="justify-center">
          <Button color="blue" onClick={handleConfirm}>Yes, confirm</Button>
          <Button color="gray" onClick={handleClose}>No, keep it</Button>
        </ModalFooter>
      </Modal>
    </>
  );
}
