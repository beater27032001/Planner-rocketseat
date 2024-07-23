import { Mail, User, X } from "lucide-react";
import Button from "../../components/button";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { api } from "../../lib/axios";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";

interface ConfirmGuestsModalProps {
  closeConfirmGuestsModal: () => void;
}

interface Trip {
  destination: string;
  endsAt: string;
  id: string;
  isConfirmed: boolean;
  ownerEmail: string;
  ownerName: string;
  startsAt: string;
}

// interface Participant {
//   id: string;
//   email: string;
//   isConfirmed: boolean;
//   name: string | null;
// }

export default function ConfirmGuestsModal({
  closeConfirmGuestsModal,
}: ConfirmGuestsModalProps) {
  const { tripId } = useParams();
  const [trip, setTrip] = useState<Trip | undefined>();

  useEffect(() => {
    api.get(`/trips/${tripId}`).then((response) => setTrip(response.data));
  }, [tripId]);

  const displayedDate = trip
    ? format(trip.startsAt, "d' de 'LLLL", { locale: ptBR })
        .concat(" até ")
        .concat(format(trip.endsAt, "d' de 'LLLL", { locale: ptBR }))
        .concat(" de ")
        .concat(format(trip.endsAt, "y"))
    : null;

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[640px] rounded-xl py-5 px-6 shadow-shape bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold">Confirmar participação</h2>
            <button>
              <X
                onClick={closeConfirmGuestsModal}
                className="size-5 text-zinc-400"
              />
            </button>
          </div>

          <p className="text-sm text-zinc-400">
            Você foi convidado(a) para participar de uma viagem para{" "}
            <span className="text-zinc-100 font-semibold">
              {trip?.destination}
            </span>{" "}
            nas datas de{" "}
            <span className="text-zinc-100 font-semibold">{displayedDate}</span>
          </p>
          <p className="text-sm text-zinc-400">
            Para confirmar sua presença na viagem, preencha os dados abaixo:
          </p>
        </div>

        <form className="space-y-3">
          <div className="h-14 px-4 bg-zinc-950 border-zinc-800 rounded-lg flex items-center gap-2">
            <User className="text-zinc-400 size-5" />
            <input
              type="text"
              name="name"
              placeholder="Seu nome completo"
              className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1"
            />
          </div>

          <div className="h-14 px-4 bg-zinc-950 border-zinc-800 rounded-lg flex items-center gap-2">
            <Mail className="text-zinc-400 size-5" />
            <input
              type="email"
              name="email"
              placeholder="Seu e-mail"
              className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1"
            />
          </div>

          <Button type="submit" variant="primary" size="full">
            {" "}
            Confirmar minha presença
          </Button>
        </form>
      </div>
    </div>
  );
}
