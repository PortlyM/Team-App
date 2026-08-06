import Link from "next/link";

export default function Logo() {
    return (
        <div className="flex items-center">
            <Link href="/" className="text-2xl font-extrabold tracking-tighter">
              TeamApp<span className="text-blue-600">.</span>
            </Link>
          </div>
    )
}