type SubmitButtonProps = {
    text: string;
}

export default function SubmitButton({text}: SubmitButtonProps) {
  return (
    <button
      type="submit"
      className="w-full py-3.5 mt-2 bg-blue-600 text-white rounded-xl font-semibold shadow-lg hover:bg-
  blue-700 hover:shadow-blue-500/30 active:scale-95 transition-all"
    >
      {text}
    </button>
  );
}
