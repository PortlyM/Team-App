"use client";

import { useState, useTransition } from "react";
import FormField from "@/components/FormField";
import SubmitButton from "@/components/SubmitButton";
import { loginAction } from "@/lib/auth";

export default function LoginForm() {
    const [error, setError] = useState<string | null>(null);
    const [isPending, startTransition] = useTransition();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        
        const formData = new FormData(e.currentTarget);
        
        startTransition(async () => {
            const result = await loginAction(formData);
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

                <SubmitButton text={isPending ? "Logging in..." : "Log in"} />
            </form>
        </div>
    )
}