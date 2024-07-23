import { useParams } from "react-router-dom";
import Button from "../../components/button";
import { api } from "../../lib/axios";
import { FormEvent, useState } from "react";

export default function InviteGuestsModal() {
  const { tripId } = useParams();
  const [email, setEmail] = useState("");

  async function createGuest(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const data = new FormData(event.currentTarget);

    const email = data.get("email")?.toString();

    await api.post(`/trips/${tripId}/invite`, {
      email,
    });

    window.document.location.reload();
  }

  return (
    <form onSubmit={createGuest} className="space-y-3">
      <div className="h-14 px-4 bg-zinc-950 border-zinc-800 rounded-lg flex items-center gap-2">
        <input
          required
          type="email"
          placeholder="Email do convidado"
          name="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          className="bg-transparent text-lg placeholder-zinc-400 w-full outline-none text-left pl-0"
        />
      </div>
      <Button type="submit" variant="primary" size="full">
        Enviar Convite
      </Button>
    </form>
  );
}
