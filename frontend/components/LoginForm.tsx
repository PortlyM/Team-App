"use client";

import { useState } from "react";

import FormField from "@/components/FormField";
import SubmitButton from "@/components/SubmitButton";

export default function LoginForm() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        console.log("Data from form: ", email, password);
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="space-y-5">
                {/* Email field */}
                <FormField
                    upperText="Email Address"
                    type="email"
                    value={email}
                    placeholder="you@email.com"
                    onChange={(e) => setEmail(e.target.value)}
                />

                {/* Password field */}
                <FormField
                    upperText="Password"
                    type="password"
                    value={password}
                    placeholder="*******"
                    onChange={(e) => setPassword(e.target.value)}
                />

                <SubmitButton text="Log in" />
            </form>
        </div>
    )
}