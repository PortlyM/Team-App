"use client"

import FormField from "@/components/FormField";
import SubmitButton from "@/components/SubmitButton";
import {useState} from "react";

export default function RegisterForm() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Data from form: ", name, email, password, repeatPassword);
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="space-y-5">
                {/* Name field */}
                <FormField
                    upperText="User name"
                    type="name"
                    value={name}
                    placeholder="Username"
                    onChange={(e) => setName(e.target.value)}
                />

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

                {/* Repeat password field */}
                <FormField
                    upperText="Password"
                    type="password"
                    value={repeatPassword}
                    placeholder="*******"
                    onChange={(e) => setRepeatPassword(e.target.value)}
                />

                <SubmitButton text="Sign up" />
            </form>
        </div>
    )
}