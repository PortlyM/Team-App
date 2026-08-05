import Logo from "@/components/logo/component";
import Link from "next/link";

export default function Home() {
  return (
    <div className="min-h-screen flex flex-col">
      {/* Navbar */}
      <header className="w-full border-b border-gray-200 dark:border-gray-800">
        <nav className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
          {/* Left: Logo */}
          <Logo />

          {/* Right: Auth Buttons */}
          <div className="flex items-center gap-6">
            <Link 
              href="/login" 
              className="text-sm font-medium hover:text-gray-500 transition-colors"
            >
              Log in
            </Link>
            <Link 
              href="/register" 
              className="px-5 py-2.5 bg-foreground text-background rounded-full text-sm font-medium hover:scale-105 transition-transform shadow-sm"
            >
              Sign up for free
            </Link>
          </div>
        </nav>
      </header>

      {/* Hero Section */}
      <main className="flex-1 flex flex-col items-center justify-center px-6 text-center">
        {/* Badge */}
        <div className="mb-8 px-4 py-1.5 rounded-full border border-gray-200 dark:border-gray-800 text-xs font-semibold uppercase tracking-wider text-gray-500 dark:text-gray-400 bg-gray-50 dark:bg-gray-900/50">
          Next-level collaboration
        </div>

        {/* Main Heading */}
        <h1 className="text-5xl md:text-7xl font-extrabold tracking-tight max-w-4xl text-balance">
          Manage your team with <br className="hidden md:block" />
          <span className="bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-cyan-500">
            incredible ease
          </span>
        </h1>

        {/* Marketing Text */}
        <p className="mt-8 text-lg md:text-xl text-gray-600 dark:text-gray-400 max-w-2xl text-balance leading-relaxed">
          Build the perfect environment for your team. Organize tasks, track progress, 
          and communicate faster than ever. Take the first step towards better productivity.
        </p>

        <div className="mt-10 flex flex-col sm:flex-row gap-4 w-full sm:w-auto">
          <Link 
            href="/register" 
            className="px-8 py-4 bg-blue-600 text-white rounded-full text-base font-semibold hover:bg-blue-700 transition-colors shadow-lg hover:shadow-blue-500/30"
          >
            Get started for free
          </Link>
        </div>
      </main>
    </div>
  );
}
