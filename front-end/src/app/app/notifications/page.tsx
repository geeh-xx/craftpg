import { revalidatePath } from "next/cache";
import { Badge, Button, Card } from "flowbite-react";
import { apiFetch } from "@/lib/api-client";
import { NotificationItem } from "@/lib/api-types";

async function markAsRead(notificationId: string) {
  "use server";
  await apiFetch<NotificationItem>(`/notifications/${notificationId}/read`, {
    method: "POST",
  });
  revalidatePath("/app/notifications");
}

export default async function NotificationsPage() {
  let notifications: NotificationItem[] = [];
  try {
    notifications = await apiFetch<NotificationItem[]>("/notifications");
  } catch {
    notifications = [];
  }

  return (
    <section className="space-y-4">
      <h1 className="text-3xl font-semibold">Notifications</h1>

      {notifications.length === 0 ? (
        <Card>No notifications.</Card>
      ) : (
        notifications.map((item) => (
          <Card key={item.id}>
            <div className="flex items-start justify-between gap-4">
              <div className="space-y-2">
                <div className="flex items-center gap-2">
                  <h2 className="font-semibold">{item.type}</h2>
                  <Badge color={item.read ? "success" : "warning"}>{item.read ? "read" : "unread"}</Badge>
                </div>
                <p className="text-sm text-slate-600 break-all">{item.payloadJson}</p>
                <p className="text-xs text-slate-500">{new Date(item.createdAt).toLocaleString()}</p>
              </div>

              {!item.read ? (
                <form action={markAsRead.bind(null, item.id)}>
                  <Button size="xs" type="submit">Mark as read</Button>
                </form>
              ) : null}
            </div>
          </Card>
        ))
      )}
    </section>
  );
}
