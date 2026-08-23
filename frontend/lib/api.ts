import { cookies } from 'next/headers';
import { redirect } from 'next/navigation';

export interface UserDto {
    id: string;
    email: string;
    name: string;
}

export interface TaskDto {
    id: string;
    content: string;
    status: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED';
    todoListId: string;
}

export interface TodoListDto {
    id: string;
    name: string;
    teamId: string;
    tasks: TaskDto[];
}

export interface TeamDto {
    id: string;
    name: string;
    leader: UserDto;
    todoLists: TodoListDto[];
    members: UserDto[];
}

const getApiUrl = () => process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

async function refreshAccessToken(refreshToken: string) {
    const res = await fetch(`${getApiUrl()}/users/refresh`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ refreshToken }),
    });

    if (!res.ok) {
        throw new Error('Refresh token failed');
    }

    return res.json();
}

export async function fetchWithAuth(url: string, options: RequestInit = {}) {
    const cookieStore = await cookies();
    const token = cookieStore.get('session_token')?.value;

    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.headers,
    };

    let res = await fetch(`${getApiUrl()}${url}`, {
        ...options,
        headers,
    });

    if (res.status === 401) {
        const refreshToken = cookieStore.get('refresh_token')?.value;
        if (!refreshToken) {
            redirect('/');
        }

        try {
            const data = await refreshAccessToken(refreshToken);
            
            cookieStore.set({
                name: 'session_token',
                value: data.token,
                httpOnly: true,
                secure: process.env.NODE_ENV === 'production',
                path: '/',
                maxAge: data.expiresIn / 1000
            });
            
            cookieStore.set({
                name: 'refresh_token',
                value: data.refreshToken,
                httpOnly: true,
                secure: process.env.NODE_ENV === 'production',
                path: '/',
                maxAge: data.expiresIn / 1000 * 30
            });

            // Retry the original request
            const newHeaders = {
                ...headers,
                'Authorization': `Bearer ${data.token}`,
            };

            res = await fetch(`${getApiUrl()}${url}`, {
                ...options,
                headers: newHeaders,
            });
        } catch (error) {
            // Refresh failed, redirect to login
            cookieStore.delete('session_token');
            cookieStore.delete('refresh_token');
            cookieStore.delete('user_id');
            redirect('/');
        }
    }

    return res;
}

// API Functions

export async function getAllTeams(): Promise<TeamDto[]> {
    const res = await fetchWithAuth('/teams');
    if (!res.ok) throw new Error('Failed to fetch teams');
    return res.json();
}

export async function getTeamById(id: string): Promise<TeamDto> {
    const res = await fetchWithAuth(`/teams/${id}`);
    if (!res.ok) throw new Error('Failed to fetch team');
    return res.json();
}

export async function joinTeam(teamId: string, userId: string): Promise<UserDto[]> {
    const res = await fetchWithAuth(`/teams/${teamId}/members/${userId}`, {
        method: 'POST',
    });
    if (!res.ok) throw new Error('Failed to join team');
    return res.json();
}

export async function leaveTeam(teamId: string, userId: string): Promise<UserDto[]> {
    const res = await fetchWithAuth(`/teams/${teamId}/members/${userId}`, {
        method: 'DELETE',
    });
    if (!res.ok) throw new Error('Failed to leave team');
    return res.json();
}

export async function createTodoList(teamId: string, name: string): Promise<TodoListDto> {
    const res = await fetchWithAuth(`/todolists/${teamId}`, {
        method: 'POST',
        body: JSON.stringify({ name }),
    });
    if (!res.ok) throw new Error('Failed to create todo list');
    return res.json();
}

export async function deleteTodoList(listId: string): Promise<void> {
    const res = await fetchWithAuth(`/todolists/${listId}`, {
        method: 'DELETE',
    });
    if (!res.ok) throw new Error('Failed to delete todo list');
}

export async function createTask(todoListId: string, content: string): Promise<TaskDto> {
    const res = await fetchWithAuth(`/todolists/tasks`, {
        method: 'POST',
        body: JSON.stringify({ content, status: 'NOT_STARTED', todoListId }),
    });
    if (!res.ok) throw new Error('Failed to create task');
    return res.json();
}

export async function updateTaskStatus(taskId: string, status: string): Promise<TaskDto> {
    const res = await fetchWithAuth(`/todolists/tasks/${taskId}`, {
        method: 'PATCH',
        body: JSON.stringify({ status }),
    });
    if (!res.ok) throw new Error('Failed to update task status');
    return res.json();
}

export async function deleteTask(taskId: string): Promise<void> {
    const res = await fetchWithAuth(`/todolists/tasks/${taskId}`, {
        method: 'DELETE',
    });
    if (!res.ok) throw new Error('Failed to delete task');
}

export async function createTeam(name: string): Promise<TeamDto> {
    const res = await fetchWithAuth('/teams', {
        method: 'POST',
        body: JSON.stringify({ name }),
    });
    if (!res.ok) throw new Error('Failed to create team');
    return res.json();
}

export async function getAllUsers(): Promise<UserDto[]> {
    const res = await fetchWithAuth('/users');
    if (!res.ok) throw new Error('Failed to fetch users');
    return res.json();
}

export async function getUser(userId: string): Promise<UserDto> {
    const res = await fetchWithAuth(`/users/${userId}`);
    if (!res.ok) throw new Error('Failed to fetch user');
    return res.json();
}

export async function getUserId(): Promise<string | undefined> {
    const cookieStore = await cookies();
    return cookieStore.get('user_id')?.value;
}
