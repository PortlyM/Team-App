'use server'
import { cookies } from 'next/headers'
import { redirect } from 'next/navigation'

export async function registerAction(formData: FormData) {
    const name = formData.get('name') as string;
    const email = formData.get('email') as string;
    const password = formData.get('password') as string;

    const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

    const res = await fetch(`${apiUrl}/users/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, email, password }),
    });

    if (!res.ok) {
        return { error: 'Failed to register account. Please try again.' };
    }
    
    // Redirect to login page upon success
    redirect('/login');
}

export async function loginAction(formData: FormData) {
    const email = formData.get('email') as string;
    const password = formData.get('password') as string;

    const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

    const res = await fetch(`${apiUrl}/users/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    });

    if (!res.ok) {
        return { error: 'Invalid email or password' };
    }

    const data = await res.json();
    
    // In Next.js 15+, cookies() is an async function
    const cookieStore = await cookies();

    cookieStore.set({
        name: 'session_token',
        value: data.token,
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        path: '/',
        maxAge: data.expiresIn / 1000 // expiresIn is usually in ms
    });

    cookieStore.set({
        name: 'refresh_token',
        value: data.refreshToken,
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        path: '/',
        maxAge: data.expiresIn / 1000 * 30 // Assuming refresh token lives longer
    });

    if (data.userId) {
        cookieStore.set({
            name: 'user_id',
            value: data.userId,
            httpOnly: true,
            secure: process.env.NODE_ENV === 'production',
            path: '/',
            maxAge: data.expiresIn / 1000 * 30
        });
    }

    redirect('/team');
}

export async function logoutAction() {
    const cookieStore = await cookies();
    cookieStore.delete('session_token');
    cookieStore.delete('refresh_token');
    cookieStore.delete('user_id');
    redirect('/');
}
