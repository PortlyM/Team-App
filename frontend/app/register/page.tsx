import Logo from "@/components/Logo";
import RegisterForm from "@/components/RegisterForm";

export default function Register() {

    
    return (
        <div className="min-h-screen flex flex-col items-center justify-center">
              <div
                className="w-full max-w-md p-8 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800
          rounded-2xl shadow-xl flex flex-col items-center"
              >
                <Logo />
                  <RegisterForm/>
              </div>
            </div>
    )
}