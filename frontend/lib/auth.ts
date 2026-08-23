'use server'
import { cookies } from 'next/headers'
import { redirect } from 'next/navigation'

export async function loginAction(formData: FormData) {
    const res = await fetch('https://twoje-api.com/login', { /* ... */ })
    const data = await res.json()

    cookies().set({
        name: 'session_token',
        value: data.token,
        httpOnly: true,
        secure: true,
        path: '/',
        maxAge: 60 * 60 * 24
    })


    redirect('/')
}
