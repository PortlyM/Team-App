'use server'

import { joinTeam, leaveTeam, getUserId, createTodoList, deleteTodoList, createTask, updateTaskStatus, deleteTask, createTeam } from "@/lib/api";
import { revalidatePath } from "next/cache";

export async function joinTeamAction(teamId: string) {
    const userId = await getUserId();
    if (!userId) throw new Error("Not logged in");
    await joinTeam(teamId, userId);
    revalidatePath("/team");
    revalidatePath(`/team/${teamId}`);
}

export async function leaveTeamAction(teamId: string) {
    const userId = await getUserId();
    if (!userId) throw new Error("Not logged in");
    await leaveTeam(teamId, userId);
    revalidatePath("/team");
    revalidatePath(`/team/${teamId}`);
}

export async function createTeamAction(formData: FormData) {
    const name = formData.get("name") as string;
    if (!name) return;
    await createTeam(name);
    revalidatePath("/team");
}

export async function createListAction(teamId: string, formData: FormData) {
    const name = formData.get("name") as string;
    if (!name) return;
    await createTodoList(teamId, name);
    revalidatePath(`/team/${teamId}`);
}

export async function deleteListAction(teamId: string, listId: string) {
    await deleteTodoList(listId);
    revalidatePath(`/team/${teamId}`);
}

export async function createTaskAction(teamId: string, listId: string, formData: FormData) {
    const content = formData.get("content") as string;
    if (!content) return;
    await createTask(listId, content);
    revalidatePath(`/team/${teamId}`);
}

export async function updateTaskStatusAction(teamId: string, taskId: string, status: string) {
    await updateTaskStatus(taskId, status);
    revalidatePath(`/team/${teamId}`);
}

export async function deleteTaskAction(teamId: string, taskId: string) {
    await deleteTask(taskId);
    revalidatePath(`/team/${teamId}`);
}
