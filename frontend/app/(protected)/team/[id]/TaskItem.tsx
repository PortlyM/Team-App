'use client'

import { TaskDto } from "@/lib/api";
import { updateTaskStatusAction, deleteTaskAction } from "../actions";
import { useTransition } from "react";

export default function TaskItem({ teamId, task }: { teamId: string, task: TaskDto }) {
    const [isPending, startTransition] = useTransition();

    const handleStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const newStatus = e.target.value;
        startTransition(() => {
            updateTaskStatusAction(teamId, task.id, newStatus);
        });
    };

    return (
        <div className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm flex items-center justify-between group">
            <span className="text-gray-800 font-medium">{task.content}</span>
            <div className="flex items-center gap-3">
                <select 
                    value={task.status} 
                    onChange={handleStatusChange}
                    disabled={isPending}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block p-2"
                >
                    <option value="NOT_STARTED">Not Started</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="COMPLETED">Completed</option>
                </select>
                <form action={deleteTaskAction.bind(null, teamId, task.id)}>
                    <button 
                        type="submit"
                        className="text-red-500 hover:bg-red-50 p-2 rounded-full transition-colors opacity-0 group-hover:opacity-100 focus:opacity-100"
                        title="Delete Task"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                          <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
                        </svg>
                    </button>
                </form>
            </div>
        </div>
    );
}
