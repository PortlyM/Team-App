type FormFieldProps = {
    upperText: string;
    type: string;
    value: string;
    placeholder: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function FormField({upperText, type, value, placeholder, onChange}: FormFieldProps) {
  return (
    <div className="space-y-2">
      <label className="text-sm font-medium text-gray-700 dark:text-gray-300">
        {upperText}
      </label>
      <input
        type={type}
        required
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className="w-full px-4 py-3 rounded-xl border border-gray-300 dark:border-gray-700 bg-gray-50
  dark:bg-gray-800 text-foreground focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none 
  transition-all"
      />
    </div>
  );
}
