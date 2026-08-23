"use client"

import FormField from "@/components/FormField";
import SubmitButton from "@/components/SubmitButton";
import { useState, useTransition } from "react";
import { registerAction } from "@/lib/auth";

export default function RegisterForm() {
    const [error, setError] = useState<string | null>(null);
    const [isPending, startTransition] = useTransition();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        
        const formData = new FormData(e.currentTarget);
        const password = formData.get("password") as string;
        const repeatPassword = formData.get("repeatPassword") as string;

        if (password !== repeatPassword) {
            setError("Passwords do not match");
            return;
        }

        startTransition(async () => {
            const result = await registerAction(formData);
            if (result?.error) {
                setError(result.error);
            }
        });
    };

    return (
        <div className="w-full mt-8">
            <form onSubmit={handleSubmit} className="space-y-5">
                {error && (
                    <div className="p-3 bg-red-100 text-red-600 rounded-lg text-sm text-center">
                        {error}
                    </div>
                )}
                
                <FormField
                    upperText="User name"
                    type="text"
                    name="name"
                    placeholder="Username"
                />

                <FormField
                    upperText="Email Address"
                    type="email"
                    name="email"
                    placeholder="you@email.com"
                />

                <FormField
                    upperText="Password"
                    type="password"
                    name="password"
                    placeholder="*******"
                />

                <FormField
                    upperText="Repeat Password"
                    type="password"
                    name="repeatPassword"
                    placeholder="*******"
                />

                <SubmitButton text={isPending ? "Signing up..." : "Sign up"} />
            </form>
        </div>
    )
}