import Logo from "@/components/Logo";
import LoginForm from "@/components/LoginForm";

export default function Login() {

  return (
    <div className="min-h-screen flex flex-col items-center justify-center">
      <div
        className="w-full max-w-md p-8 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800
  rounded-2xl shadow-xl flex flex-col items-center"
      >
        <Logo />
        <LoginForm />
      </div>
    </div>
  );
}
