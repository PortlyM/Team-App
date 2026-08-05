"use client"

import { useState } from "react";

import Logo from "@/components/logo/component";
import FormField from "@/components/formField/component";
import SubmitButton from "@/components/submitButton/component";

export default function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    
    const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Data from form: ", name, email, password, repeatPassword);
  };
    
    return (
        <div className="min-h-screen flex flex-col items-center justify-center">
              <div
                className="w-full max-w-md p-8 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800
          rounded-2xl shadow-xl flex flex-col items-center"
              >
                <Logo />
        
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
            </div>
    )
}