import Logo from "@/components/Logo";
import Link from "next/link";
import { logoutAction } from "@/lib/auth";

export default function ProtectedLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex h-screen bg-gray-50 text-gray-900">
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col">
        <div className="p-6 border-b border-gray-200">
          <Logo />
        </div>
        
        <nav className="flex-1 p-4 space-y-2">
          <Link 
            href="/dashboard" 
            className="block px-4 py-3 rounded-lg text-gray-700 hover:bg-gray-100 font-medium transition-colors"
          >
            Dashboard
          </Link>
          <Link 
            href="/team" 
            className="block px-4 py-3 rounded-lg bg-blue-50 text-blue-700 hover:bg-blue-100 font-medium transition-colors"
          >
            Teams
          </Link>
        </nav>

        <div className="p-4 border-t border-gray-200">
          <form action={logoutAction}>
            <button 
              type="submit"
              className="w-full text-left px-4 py-3 rounded-lg text-red-600 hover:bg-red-50 font-medium transition-colors"
            >
              Logout
            </button>
          </form>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-auto">
        {children}
      </main>
    </div>
  );
}
