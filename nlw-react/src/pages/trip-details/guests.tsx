import { CheckCircle2, CircleDashed } from "lucide-react";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { api } from "../../lib/axios";

interface Participant {
  email: string;
  id: string;
  isConfirmed: boolean;
  name: string | null;
}

export default function Guests() {
  const { tripId } = useParams();
  const [participants, setParticipants] = useState<Participant[]>();

  useEffect(() => {
    api
      .get(`/trips/${tripId}/participants`)
      .then((response) => setParticipants(response.data));
  }, [tripId]);

  return (
    <div className="space-y-5">
      {participants?.map((participant) => {
        return (
          <div
            key={participant.id}
            className="flex items-center justify-between gap-4"
          >
            <div className="space-y-1.5">
              <span className="block font-medium text-zinc-100">
                {participant.name}
              </span>
              <span className="block text-sm text-zinc-400 truncate">
                {participant.email}
              </span>
            </div>
            {participant.isConfirmed ? (
              <CheckCircle2 className="text-green-400 size-5 shrink-0" />
            ) : (
              <CircleDashed className="text-zinc-400 size-5 shrink-0" />
            )}
          </div>
        );
      })}
    </div>
  );
}
