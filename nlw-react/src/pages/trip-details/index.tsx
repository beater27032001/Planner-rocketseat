import { Plus, UserCog } from "lucide-react";
import { useState } from "react";
import CreateActivityModal from "./create-activity-modal";
import ImportantsLinks from "./importants-links";
import Guests from "./guests";
import Activities from "./activities";
import DestinationAndDateHeader from "./destination-and-date-header";
import CreateLinkModal from "./create-link-modal";
import ConfirmGuestsModal from "./create-confirm-guests-modal";
import Button from "../../components/button";
import InviteGuestsModal from "./invite-guests-modal";

export default function TripDetailsPage() {
  const [isCreateActivityModalOpen, setIsCreateActivityModalOpen] =
    useState(false);

  const [isCreateLinkModalOpen, setIsCreateLinkModalOpen] = useState(false);

  const [isConfirmGuestsModalOpen, setIsConfirmGuestsModalOpen] =
    useState(false);

  function openCreateActivityModal() {
    setIsCreateActivityModalOpen(true);
  }

  function closeCreateActivityModal() {
    setIsCreateActivityModalOpen(false);
  }

  function openCreateLinkModal() {
    setIsCreateLinkModalOpen(true);
  }

  function closeCreateLinkModal() {
    setIsCreateLinkModalOpen(false);
  }

  function openConfirmGuestsModal() {
    setIsConfirmGuestsModalOpen(true);
  }

  function closeConfirmGuestsModal() {
    setIsConfirmGuestsModalOpen(false);
  }

  return (
    <div className="max-w-6xl px-6 py-10 mx-auto space-y-8">
      <DestinationAndDateHeader />

      <main className="flex gap-16 px-4">
        <div className="flex-1 space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-3xl font-semibold">Atividades</h2>
            <button
              onClick={openCreateActivityModal}
              className="bg-lime-300 text-lime-950 rounded-lg px-5 py-2 font-medium flex items-center gap-2 hover:bg-lime-400"
            >
              <Plus className="size-5" />
              Cadastrar atividade
            </button>
          </div>

          <Activities />
        </div>

        <div className="w-80 space-y-6">
          <div className="space-y-6">
            <h2 className="font-semibold text-xl">Links importantes</h2>
            <ImportantsLinks />
            <Button
              onClick={openCreateLinkModal}
              variant="secondary"
              size="full"
            >
              <Plus className="size-5" />
              Cadastrar novo link
            </Button>
          </div>

          <div className="2-full h-px bg-zinc-800" />

          <div>
            <h2 className="font-semibold text-xl">Convidar</h2>
            <InviteGuestsModal />
            
          </div>

          <div className="2-full h-px bg-zinc-800" />

          <div className="space-y-6">
            <h2 className="font-semibold text-xl">Convidados</h2>
            <Guests />
            <Button
              onClick={openConfirmGuestsModal}
              variant="secondary"
              size="full"
            >
              <UserCog className="size-5" />
              Gerenciar convidades
            </Button>
          </div>
        </div>
      </main>

      {isCreateActivityModalOpen && (
        <CreateActivityModal
          closeCreateActivityModal={closeCreateActivityModal}
        />
      )}

      {isCreateLinkModalOpen && (
        <CreateLinkModal closeCreateLinkModal={closeCreateLinkModal} />
      )}

      {isConfirmGuestsModalOpen && (
        <ConfirmGuestsModal closeConfirmGuestsModal={closeConfirmGuestsModal} />
      )}
    </div>
  );
}
